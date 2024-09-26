package com.example.newsappcompose.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsappcompose.domain.model.Article
import com.example.newsappcompose.domain.usecase.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
):ViewModel(){

    private val _newsRefresh = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val newsRefresh: StateFlow<PagingData<Article>> = _newsRefresh

    val news = newsUseCases.getNews(
        sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
    ).cachedIn(viewModelScope)

    fun refreshNews() {
        viewModelScope.launch {
            newsUseCases.getNews(
                sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
            ).cachedIn(viewModelScope).collectLatest {
                _newsRefresh.value = it
            }
        }
    }
}