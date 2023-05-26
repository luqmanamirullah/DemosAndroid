package com.example.demos.api

import com.example.demos.models.newsFromInternet.NewsInternet
import com.example.demos.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Interface2 {
    @GET("top-headlines")
    suspend fun getNewsData(
        @Query("category") categoryCode: String,
        @Query("q") qCode: String = "",
        @Query("country") countryCode: String = "us",
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): Response<NewsInternet>
}