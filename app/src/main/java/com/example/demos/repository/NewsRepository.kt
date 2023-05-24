package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.database.NewsDatabase

class NewsRepository(db: NewsDatabase) {
    suspend fun getNews(category: String,q: String) =
        RetrofitInstance.newsApi.getNewsData(category,q)
    }