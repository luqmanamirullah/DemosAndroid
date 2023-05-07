package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.database.NewsDatabase

class HomeRepository(
    val db: NewsDatabase
) {
    suspend fun getNews() =
        RetrofitInstance.api.getNews()
}