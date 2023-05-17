package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.models.login.LoginRequest

class LoginRepository{

    suspend fun login(email: String, password: String) =
        RetrofitInstance.api.login(LoginRequest(email, password))

}