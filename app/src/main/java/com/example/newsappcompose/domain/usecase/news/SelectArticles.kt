package com.example.newsappcompose.domain.usecase.news

import com.example.newsappcompose.data.local.NewsDao
import com.example.newsappcompose.domain.model.Article
import com.example.newsappcompose.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class SelectArticles(
    private val newsRepository: NewsRepository,
) {

     operator fun invoke():Flow<List<Article>>{
        return newsRepository.getArticles()
    }
}