package com.example.newsappcompose.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsappcompose.domain.model.Article
import com.example.newsappcompose.domain.usecase.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val useCases: NewsUseCases
):ViewModel(){

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event:DetailsEvent){
        when(event){
            is DetailsEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    val article = useCases.selectArtic(event.article.url)

                    if(article == null ){
                        upsertArticle(event.article)
                    } else {
                        deleteArticle(event.article)
                    }
                }
            }
            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private suspend fun deleteArticle(article: Article) {
        useCases.deleteArticle(article)
        sideEffect = "Article Deleted"

    }

    private suspend fun upsertArticle(article: Article) {
        useCases.upsertArticle(article)
        sideEffect = "Article Saved"
    }

}