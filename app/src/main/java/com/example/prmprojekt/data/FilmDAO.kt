package com.example.prmprojekt.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDAO {
    @Insert
    suspend fun insertFilm(film: FilmEntity)

    @Update
    suspend fun updateFilm(film: FilmEntity)

    @Delete
    suspend fun deleteFilm(film: FilmEntity)

    @Query("SELECT * FROM FilmEntity")
    fun getAllFilms():Flow<List<FilmEntity>>
}