package com.example.demos.database

import androidx.room.TypeConverter
import com.example.demos.models.newsFromInternet.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name,name)
    }
}