package com.example.demos.models.search

import com.example.demos.models.news.News
import com.example.demos.models.policy.Policy

data class Everythings(
    val news: List<News>,
    val policy: List<Policy>
)