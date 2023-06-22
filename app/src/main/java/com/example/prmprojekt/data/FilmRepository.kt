package com.example.prmprojekt.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import java.lang.NullPointerException

class FilmRepository() {
    val film_entities = "film_entities"
    val firestore = FirebaseFirestore.getInstance()
    val films // = dao.getAllFilms()
        get() = FirebaseFirestore.getInstance().collection(film_entities)
            .dataObjects<FilmEntity>()

    suspend fun insert(film: FilmEntity) {
        film.id = firestore.collection(film_entities).document().getId()
        firestore.collection(film_entities).document(film.id!!)
            .set(film).addOnSuccessListener {
                Log.d("FB", "onCreate success: ${film}")
            }.addOnFailureListener {
                Log.d("FB", "onCreate failure:")
            }
    }

    suspend fun update(film: FilmEntity){
        film.id ?: run {
            throw NullPointerException("provided ${FilmEntity::class.simpleName} malformated: id field is null")
        }

        firestore.collection(film_entities).document(film.id!!).get().addOnSuccessListener {
            firestore.collection(film_entities).document(film.id!!).set(film).addOnFailureListener{
                Log.d("FB", "failed to update ${FilmEntity::class.simpleName} class ${it.message}")
            }
        }

    }

    suspend fun delete(film: FilmEntity) {
        film.id ?: run{
            throw NullPointerException("provided ${FilmEntity::class.simpleName} malformated: id field is null")
        }
        firestore.collection(film_entities).document(film.id!!).delete().addOnFailureListener{
            Log.d("FB", "film ${film.nazwa} not added to Database ${it.message}")
        }.addOnCanceledListener {
            Log.d("FB", "film ${film.nazwa} cancelled")
        }.addOnSuccessListener {
            Log.d("FB", "film ${film.nazwa} succeded")
        }
    }
}