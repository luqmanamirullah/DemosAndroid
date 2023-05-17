package com.example.demos.models.login

data class Login(
    val access_token: String,
    val `data`: User,
    val message: String,
    val token_type: String
)

data class LoginRequest(
    val user_email : String,
    val password: String
)