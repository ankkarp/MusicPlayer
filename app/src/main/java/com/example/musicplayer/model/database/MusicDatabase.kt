package com.example.musicplayer.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.model.dao.MusicDao
import com.example.musicplayer.model.data.MusicItem

@Database(entities = [(MusicItem::class)], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun musicDao(): MusicDao

    companion object {
        @Volatile
        private var Instance: MusicDatabase? = null

        fun getDatabase(context: Context): MusicDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MusicDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}