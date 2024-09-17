package com.example.newsappcompose.domain.usecase.news

import com.example.newsappcompose.data.local.NewsDao
import com.example.newsappcompose.domain.model.Article
import com.example.newsappcompose.domain.repository.NewsRepository

class SelectArtic(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(url: String):Article?{
        return newsRepository.getArticle(url)
    }
}