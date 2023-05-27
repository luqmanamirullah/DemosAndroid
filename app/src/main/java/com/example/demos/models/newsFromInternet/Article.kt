package com.example.demos.models.newsFromInternet

import androidx.room.Entity
import androidx.room.PrimaryKey
<<<<<<< HEAD
import java.io.Serializable

@Entity (
    tableName = "news"
        )
data class Article(
    @PrimaryKey (autoGenerate = true)
    var id: Int? = null,
    val author: String,
    val content: String?,
    val description: String?,
=======

@Entity (
    tableName = "news"
)

data class Article(
    @PrimaryKey (autoGenerate = true)
    val id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
>>>>>>> origin/news-app
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
<<<<<<< HEAD
    val urlToImage: String?
): Serializable {
=======
    val urlToImage: String
){
>>>>>>> origin/news-app
    companion object {
        fun getFirstThreeArticles(articles: List<Article>): List<Article> {
            return articles.take(3)
        }
    }
<<<<<<< HEAD
=======

>>>>>>> origin/news-app
}