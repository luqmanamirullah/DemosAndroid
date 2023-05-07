package com.example.demos.api

import com.example.demos.models.news.NewsLists
import com.example.demos.models.trending.TrendingLists
import retrofit2.Response
import retrofit2.http.GET

interface Interface {
    @GET("news")
    suspend fun getNews() : Response<NewsLists>

    @GET("trending-lists")
    suspend fun getTrends() : Response<TrendingLists>
}