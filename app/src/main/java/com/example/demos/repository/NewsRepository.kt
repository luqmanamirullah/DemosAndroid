package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.database.NewsDatabase
import com.example.demos.models.newsFromInternet.Article

class NewsRepository(val db: NewsDatabase) {
    suspend fun getNews(category: String,q: String) =
        RetrofitInstance.newsApi.getNewsData(category,q)

    suspend fun upsert(article: Article) = db.getNewsDao().upsert(article)

    fun getSavedNews() = db.getNewsDao().getAllNews()

    suspend fun deleteNews(article: Article) = db.getNewsDao().deleteArticle(article)
}