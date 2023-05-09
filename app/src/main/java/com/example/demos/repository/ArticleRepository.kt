package com.example.demos.repository

import com.example.demos.api.RetrofitInstance

class ArticleRepository {
    suspend fun getNewsDetails(newsId: Int) =
        RetrofitInstance.api.getNewsDetails(newsId)
}