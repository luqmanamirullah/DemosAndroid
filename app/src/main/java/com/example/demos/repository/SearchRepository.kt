package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.database.NewsDatabase

class SearchRepository(db: NewsDatabase) {
    suspend fun searchNews(query: String) =
        RetrofitInstance.newsApi.searchNews(query)
}