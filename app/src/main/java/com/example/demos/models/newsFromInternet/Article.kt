package com.example.demos.models.newsFromInternet

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (
    tableName = "news"
)

data class Article(
    @PrimaryKey (autoGenerate = true)
    val id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
){
    companion object {
        fun getFirstThreeArticles(articles: List<Article>): List<Article> {
            return articles.take(3)
        }
    }

}