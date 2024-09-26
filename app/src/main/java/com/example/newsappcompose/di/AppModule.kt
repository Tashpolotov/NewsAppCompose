package com.example.newsappcompose.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.room.Room
import com.example.newsappcompose.data.local.NewsDao
import com.example.newsappcompose.data.local.NewsDataBase
import com.example.newsappcompose.data.local.NewsTypeConverter
import com.example.newsappcompose.data.manager.LocalUserManagerImpl
import com.example.newsappcompose.data.remote.dto.NewsApi
import com.example.newsappcompose.data.repository.NewsRepositoryImpl
import com.example.newsappcompose.domain.manager.LocalUserManager
import com.example.newsappcompose.domain.repository.NewsRepository
import com.example.newsappcompose.domain.usecase.app_entry.AppEntryUseCases
import com.example.newsappcompose.domain.usecase.app_entry.ReadAppEntry
import com.example.newsappcompose.domain.usecase.app_entry.SaveAppEntry
import com.example.newsappcompose.domain.usecase.news.DeleteArticle
import com.example.newsappcompose.domain.usecase.news.GetNews
import com.example.newsappcompose.domain.usecase.news.NewsUseCases
import com.example.newsappcompose.domain.usecase.news.SearchNews
import com.example.newsappcompose.domain.usecase.news.SelectArtic
import com.example.newsappcompose.domain.usecase.news.SelectArticles
import com.example.newsappcompose.domain.usecase.news.UpsertArticle
import com.example.newsappcompose.util.Constants.BASE_URL
import com.example.newsappcompose.util.Constants.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application
    ):LocalUserManager = LocalUserManagerImpl(application)


    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Singleton
    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                var response: Response? = null
                var isConnected = false
                var tryCount = 0

                while (!isConnected) {
                    try {
                        response = chain.proceed(request)
                        isConnected = true
                    } catch (e: IOException) {
                        if (!isNetworkAvailable(context)) {
                            tryCount++
                            Log.d("NetworkModule", "Attempt $tryCount: Retrying connection...")
                        } else {
                            throw e
                        }
                    }
                }

                response ?: throw IOException("Failed to connect to server")
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
/*
    @Provides
    @Singleton
    fun provideNewsApi():NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }*/

    @Provides
    fun provideApiService(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi,
        newsDao: NewsDao
    ): NewsRepository = NewsRepositoryImpl(newsApi, newsDao)

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository,
        newsDao: NewsDao
    ):NewsUseCases{
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository),
            upsertArticle = UpsertArticle(newsRepository),
            deleteArticle = DeleteArticle(newsRepository),
            selectArticle = SelectArticles(newsRepository),
            selectArtic = SelectArtic(newsRepository)

        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ): NewsDataBase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDataBase::class.java,
            name = NEWS_DATABASE_NAME
            ).addTypeConverter(NewsTypeConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        newsDataBase: NewsDataBase
    ) : NewsDao = newsDataBase.newsDao

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }
}