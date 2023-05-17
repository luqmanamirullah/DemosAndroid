package com.example.demos.repository

import com.example.demos.api.RetrofitInstance

class LogoutRepository {

    suspend fun logout(token: String) =
        RetrofitInstance.api.logout(token)

}