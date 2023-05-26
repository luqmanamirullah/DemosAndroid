package com.example.demos.models.newsFromInternet

data class NewsInternet(
    val status: String,
    val articles: List<Article>,
    val totalResults: Int
)