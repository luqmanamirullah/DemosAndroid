package com.example.demos.models.policy

data class DetailsPolicy(
    val policy_id: Int,
    val appointed_at: String?,
    val created_at: String,
    val entity: String,
    val explanation: String,
    val impacts: List<Any>,
    val number: Int,
    val policy_appointed_with: List<Any>,
    val policy_changeds: List<Any>,
    val policy_changes: List<Any>,
    val policy_repeals: List<Any>,
    val source: String,
    val theme: String,
    val title: String,
    val type: String,
    val updated_at: String?,
    val year: String
)