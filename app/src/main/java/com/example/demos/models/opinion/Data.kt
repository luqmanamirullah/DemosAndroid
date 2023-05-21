package com.example.demos.models.opinion

data class Data(
    val created_at: String,
    val id: Int,
    val opinion_content: String,
    val opinion_status: String,
    val policy_id: String,
    val updated_at: Any,
    val user_id: Int
)