package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.models.login.LoginGoogleRequest
import com.example.demos.models.login.LoginRequest

class LoginRepository{

    suspend fun login(email: String, password: String) =
        RetrofitInstance.api.login(LoginRequest(email, password))

    suspend fun loginGoogle(accountEmail: String?, accountId: String?, accountName: String?, accountPhoto: String?) =
        RetrofitInstance.api.loginGoogle(LoginGoogleRequest(accountEmail, accountId, accountName, accountPhoto))
}