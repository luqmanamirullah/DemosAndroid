package com.example.demos.models.login

data class User(
    val created_at: String?,
    val id: Int,
    val updated_at: String?,
    val user_email: String?,
    val user_name: String?,
    val user_verify: Int,
    val user_picture: String?
)