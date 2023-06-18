package com.example.prmprojekt

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailFilm(navController: NavController, filmId: Int){
    Text(text = "film id $filmId")
}