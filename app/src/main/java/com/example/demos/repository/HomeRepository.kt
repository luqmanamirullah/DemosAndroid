package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.database.NewsDatabase

class HomeRepository() {
    suspend fun getTrendLists() =
        RetrofitInstance.api.getTrends()
    suspend fun getNews(newsType: String?) =
        RetrofitInstance.api.getNews(newsType)

    suspend fun getNewsType() =
        RetrofitInstance.api.getNewsType()

    suspend fun search(searchQuery: String) =
        RetrofitInstance.api.search(searchQuery)
}