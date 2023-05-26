package com.example.demos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demos.models.newsFromInternet.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}