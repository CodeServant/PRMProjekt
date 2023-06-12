package com.example.prmprojekt

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun ListScreen(navController: NavController, films: MutableList<Film>, onDeleteFilm: (Film) -> Unit) {

    val context = LocalContext.current
    var isSortedSelected by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavDestination.Add.route) },
                backgroundColor = Color(0xff64cc30)
            )
            {
                Icon(Icons.Default.Add, contentDescription = null)
            }


        },
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.list_top_app_title)) },
                actions = {
                    IconButton(onClick = { isSortedSelected = !isSortedSelected }) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.border(
                                if (isSortedSelected) 2.dp else -1.dp,
                                Color.Red,
                                CircleShape
                            ),
                            )
                    }
                }
            )

        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            FilmList(
                films = films,
                navController = navController,
                isSortedSelected = isSortedSelected,
                onDeleteFilm = onDeleteFilm
            )
            Text(
                text = "${context.getString(R.string.quantity_sum)} ${films.size}",
                fontSize = 30.sp,
                modifier = Modifier.padding(15.dp)
            )
        }

    }
}


@Composable
fun FilmList(
    navController: NavController,
    films: MutableList<Film>,
    isSortedSelected: Boolean,
    onDeleteFilm: (Film) -> Unit
) {
    val ctx = LocalContext.current
    var visibleAlertDialog by remember {
        mutableStateOf(false)
    }
    var filmSelected by remember {
        mutableStateOf(0)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .padding(10.dp)
    ) {
        items(if (isSortedSelected) films.sortedBy { it.nazwa } else films,
            key = { film -> film.id }) { film ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                val route = NavDestination.DetailsFilm.createRoute(film.id)
                                navController.navigate(route = route)
                            },
                            onLongPress = {
                                visibleAlertDialog = true
                                filmSelected = films.getFilmById(film.id)
                            }
                        )
                    }

            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                ) {
                    AsyncImage(
                        model = film.url,
                        contentDescription = "poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.aspectRatio(0.70f, true),
                        placeholder = rememberVectorPainter(Icons.Default.Refresh),
                        error = painterResource(R.drawable.ic_android_black_24dp)
                    )

                    Text(
                        text = "${film.nazwa}",
                        modifier = Modifier.align(Alignment.Top)
                            .fillMaxWidth(0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(modifier = Modifier.fillMaxHeight()
                        .requiredWidth(30.dp)) {
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
    if (visibleAlertDialog)
        AlertDialog(
            onDismissRequest = {
                visibleAlertDialog = false
            },
            title = { Text(text = ctx.getString(R.string.delete_confirmation)) },
            confirmButton = {
                Button(onClick = {
                    val index = filmSelected

                    onDeleteFilm(films[index])

                    visibleAlertDialog = false
                }) {
                    Text(text = ctx.getString(R.string.button_delete))
                }
            }
        )
}