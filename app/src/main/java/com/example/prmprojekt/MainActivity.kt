    package com.example.prmprojekt

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.prmprojekt.ui.theme.PRMProjektTheme

class MainActivity : ComponentActivity() {
    var films = mutableListOf<Film>(
        Film("Avatar", 7.toBigDecimal()),
        Film("Titanic", 9.toBigDecimal()),
        Film("Harry Potter i Więzień Azkabanu", 6.4.toBigDecimal()),
        Film("Marry Popins", 8.5.toBigDecimal()),
        Film("John Wick 4", 7.8.toBigDecimal()),
        Film("Avatar: Istota Wody", 7.toBigDecimal()),
        Film("Hellboy", 4.5.toBigDecimal()),
        Film("Kapitan Ameryka", 7.6.toBigDecimal()),
        Film("Avengers", 7.9.toBigDecimal())
    )
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRMProjektTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        floatingActionButton = {
                            FloatButtonList()
                        }
                    ){
                        LazyColumn {
                            items(films.size){
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.Blue)
                                    .clickable {

                                    }) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

