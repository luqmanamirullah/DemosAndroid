package com.example.demos.models.opinion

data class Opinion(
    val id: Int,
    val opinion_content: String,
    val opinion_status: Int,
    val user_bio: String?,
    val user_name: String,
    val user_photo: String,
    val user_verify: Int,
)

data class OpinionRequest(
    val content: String,
    val isAgree: Int
)