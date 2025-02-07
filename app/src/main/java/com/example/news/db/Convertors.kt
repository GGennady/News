package com.example.news.db

import androidx.room.TypeConverter
import com.example.news.models.Source

class Convertors {

    // function which define what happens if we want to convert to String from Source
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    // function which convert to Source
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}