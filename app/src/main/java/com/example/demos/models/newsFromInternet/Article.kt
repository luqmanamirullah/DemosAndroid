package com.example.demos.models.newsFromInternet

import androidx.annotation.Nullable
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "news"
)
data class Article(
    @PrimaryKey (autoGenerate = true)
    val id: Int? = null,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: List<String>?,
    val description: String,
    val image_url: String,
    val keywords: List<String>,
    val language: String,
    val link: String,
    val pubDate: String,
    val source_id: String,
    val title: String,
    val video_url: String?
){
    companion object {
        fun getFirstThreeArticles(articles: List<Article>): List<Article> {
            return articles.take(3)
        }
    }
}