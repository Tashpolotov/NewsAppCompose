package com.example.newsappcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsappcompose.data.local.NewsDao
import com.example.newsappcompose.data.remote.NewsPagingSource
import com.example.newsappcompose.data.remote.SearchNewsPagingSource
import com.example.newsappcompose.data.remote.dto.NewsApi
import com.example.newsappcompose.domain.model.Article
import com.example.newsappcompose.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class NewsRepositoryImpl(
    private val newsApi:NewsApi,
    private val dao:NewsDao
):NewsRepository {

    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
         return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    searchQuery = searchQuery,
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override suspend fun upsertArticle(article: Article) {
    dao.upsert(article)

    }

    override suspend fun deleteArticle(article: Article) {
        dao.delete(article)
    }

    override fun getArticles(): Flow<List<Article>> {
        return dao.getArticles()
    }

    override suspend fun getArticle(url: String): Article? {
        return dao.getArticle(url)
    }
}