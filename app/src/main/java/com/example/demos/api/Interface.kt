package com.example.demos.api

import com.example.demos.models.news.NewsLists
import com.example.demos.models.news.NewsType
import com.example.demos.models.trending.TrendingLists
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Interface {
    @GET("breakingNews")
    suspend fun getNews(
        @Query("type")
        newsType: String? = null
    ) : Response<NewsLists>

    @GET("news-type")
    suspend fun getNewsType() : Response<NewsType>

    @GET("trending-lists")
    suspend fun getTrends() : Response<TrendingLists>

    @GET("everything")
    suspend fun search(
        @Query("q")
        searchQuery: String = ""
    ): Response<NewsLists>
}