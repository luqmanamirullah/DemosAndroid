package com.example.demos.models.news

data class Article(
    val author: String,
    val content: String,
    val created_at: String,
    val id: Int,
    val image_url: String,
    val title: String,
    val type: String,
    val view: String
)