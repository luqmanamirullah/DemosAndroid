package com.example.demos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demos.models.news.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<News>>

}