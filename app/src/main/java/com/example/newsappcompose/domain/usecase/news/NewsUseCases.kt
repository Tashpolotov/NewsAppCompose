package com.example.newsappcompose.domain.usecase.news

data class NewsUseCases(
    val getNews: GetNews,
    val searchNews: SearchNews,
    val upsertArticle: UpsertArticle,
    val selectArticle: SelectArticles,
    val deleteArticle: DeleteArticle,
    val selectArtic: SelectArtic
)