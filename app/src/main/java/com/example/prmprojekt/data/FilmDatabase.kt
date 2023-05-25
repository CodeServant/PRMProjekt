package com.example.prmprojekt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FilmEntity::class], version = 1)
abstract class FilmDatabase : RoomDatabase() {
    abstract val filmDAO: FilmDAO

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getintance(context: Context): FilmDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FilmDatabase::class.java,
                        "film_database"
                    ).build()

                }
                return instance
            }
        }
    }
}