package com.example.demos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demos.models.news.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(news: News): Long

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<News>>

    @Delete
    suspend fun hideNews(news: News)
}