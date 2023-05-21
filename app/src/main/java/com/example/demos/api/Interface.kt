package com.example.demos.api

import com.example.demos.models.Logout
import com.example.demos.models.login.Login
import com.example.demos.models.login.LoginGoogleRequest
import com.example.demos.models.login.LoginRequest
import com.example.demos.models.news.ArticleData
import com.example.demos.models.news.NewsLists
import com.example.demos.models.news.NewsType
import com.example.demos.models.opinion.CreateOpinion
import com.example.demos.models.opinion.OpinionRequest
import com.example.demos.models.opinion.Opinions
import com.example.demos.models.policy.DetailsPolicyLists
import com.example.demos.models.policy.Policies
import com.example.demos.models.policy.PolicyFile
import com.example.demos.models.search.Everythings
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

    @POST("login-google")
    suspend fun loginGoogle(
        @Body loginRequest: LoginGoogleRequest,
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
    ): Response<Everythings>

    @GET("policy")
    suspend fun policy(
        @Header("Authorization") token: String
    ): Response<Policies>

    @GET("policy-details/{id}")
    suspend fun policyDetails(
        @Path("id") policyId: Int,
        @Header("Authorization") token: String
    ): Response<DetailsPolicyLists>

    @GET("policy-file/{id}")
    suspend fun policyFile(
        @Path("id") policyId: Int,
        @Header("Authorization") token: String
    ): Response<PolicyFile>

    @GET("opinions/{policyId}")
    suspend fun opinions(
        @Path("policyId") policyId: Int,
        @Header("Authorization") token: String
    ): Response<Opinions>

    @POST("create-opinion/{policyId}")
    suspend fun createOpinion(
        @Path("policyId") policyId: Int,
        @Header("Authorization") token: String,
        @Body opinionRequest: OpinionRequest
    ): Response<CreateOpinion>
}