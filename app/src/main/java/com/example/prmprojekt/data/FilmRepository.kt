package com.example.prmprojekt.data

import com.example.prmprojekt.Film

class FilmRepository (private val dao: FilmDAO) {
    val films = dao.getAllFilms()
    suspend fun insert(film: FilmEntity){
        dao.insertFilm(film)
    }
    suspend fun update(film: FilmEntity){
        dao.updateFilm(film)
    }
    suspend fun delete(film: FilmEntity){
        dao.deleteFilm(film)
    }
}