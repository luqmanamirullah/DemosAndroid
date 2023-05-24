package com.example.demos.models.newsFromInternet

data class NewsInternet(
    val nextPage: String,
    val results: List<Article>,
    val status: String,
    val totalResults: Int
)