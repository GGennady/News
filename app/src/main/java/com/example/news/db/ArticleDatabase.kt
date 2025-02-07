package com.example.news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Convertors::class)
abstract class ArticleDatabase: RoomDatabase() {

    // function that returns an article DAO
    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile // other threads can immediately see when a thread changes this instance
        private var instance: ArticleDatabase? = null

        // use LOCK to synchronize setting that instance, to be sure that there is only a single instance of database at once
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}