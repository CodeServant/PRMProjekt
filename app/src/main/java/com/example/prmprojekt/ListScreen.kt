package com.example.prmprojekt

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ListScreen(navController: NavController){
    var films = mutableListOf<Film>(
        Film("Avatar", 7.toBigDecimal(),1),
        Film("Titanic", 9.toBigDecimal(), 6),
        Film("Harry Potter i Więzień Azkabanu", 6.4.toBigDecimal(), 4),
        Film("Marry Popins", 8.5.toBigDecimal(), 5),
        Film("John Wick 4", 7.8.toBigDecimal(), 3),
        Film("Avatar: Istota Wody", 7.toBigDecimal(), 2),
        Film("Hellboy", 4.5.toBigDecimal(), 7),
        Film("Kapitan Ameryka", 7.6.toBigDecimal(),8),
        Film("Avengers", 7.9.toBigDecimal(),9)
    )
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatButtonList()
        }

    ) {
        Column(modifier = Modifier.padding(it)) {

            FilmList(films = films, navController= navController)
            Text(text = "${context.getString(R.string.quantity_sum)} ${films.size}", fontSize = 30.sp)

        }

    }
}

@Composable
fun FilmList(navController: NavController, films: List<Film>) {

    LazyColumn(modifier = Modifier
        .fillMaxHeight(0.85f)
        .padding(10.dp)) {

        items(films) {
            val film=it
            Column(modifier = Modifier
                .clickable {
                    val route = NavDestination.DetailsFilm.createRoute(it.id) // to jest raczej ok
                    navController.navigate(route = route)
                }
                .fillMaxWidth()
                .height(80.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                ) {

                    Text(text = "${film.nazwa}", modifier = Modifier.align(Alignment.CenterVertically))

                    Row(modifier = Modifier.fillMaxHeight()){
                        Text(text = "${film.rating}", modifier = Modifier.align(Alignment.Bottom))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.align(Alignment.Bottom)

                        )
                    }

                }


            }
        }


    }
}