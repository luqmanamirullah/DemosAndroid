package com.example.demos.api.example

import com.example.demos.models.Example
import retrofit2.Response
import retrofit2.http.GET

interface ExampleApi {
    @GET("v_data.php")
    suspend fun getExamples(): Response<List<Example>>
}