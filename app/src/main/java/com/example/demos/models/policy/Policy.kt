package com.example.demos.models.policy

data class Policy(
    val policy_id: Int,
    val opinion_count: String,
    val policy_file: String,
    val policy_source: String,
    val policy_theme: String,
    val policy_title: String
)