package com.example.demos.api

import com.example.demos.models.Logout
import com.example.demos.models.login.Login
import com.example.demos.models.login.LoginRequest
import com.example.demos.models.news.ArticleData
import com.example.demos.models.news.NewsLists
import com.example.demos.models.news.NewsType
import com.example.demos.models.trending.TrendingLists
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Interface {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<Login>

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token : String
    ): Response<Logout>

    @GET("breakingNews")
    suspend fun getNews(
        @Query("type")
        newsType: String? = null
    ) : Response<NewsLists>

    @GET("news-type")
    suspend fun getNewsType() : Response<NewsType>

    @GET("news/{id}")
    suspend fun getNewsDetails(
        @Path("id")
        newsId: Int
    ): Response<ArticleData>

    @GET("trending-lists")
    suspend fun getTrends() : Response<TrendingLists>

    @GET("everything")
    suspend fun search(
        @Query("q")
        searchQuery: String = ""
    ): Response<NewsLists>
}