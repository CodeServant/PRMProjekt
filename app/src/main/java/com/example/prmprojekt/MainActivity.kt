package com.example.prmprojekt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prmprojekt.ui.theme.PRMProjektTheme
import kotlin.streams.toList

// todo: dodawanie do bazy danych
// todo: udostępnienie danych przeez SMS lub mail
// todo: sprawdzanie danych które podaje/zmienia użytkownik
// todo: sprawdzenie czy link żeczywiście prowadzą do obrazów
sealed class NavDestination(val route: String) {
    object List : NavDestination("list")
    object Add : NavDestination("add")
    object Edit : NavDestination("edit/{filmId}") {
        fun createRoute(filmId: Int) = "edit/$filmId"
    }

    object DetailsFilm : NavDestination("detailsfilm/{filmId}") {
        fun createRoute(filmId: Int) = "detailsfilm/$filmId"
    }
}

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRMProjektTheme {
                val navController = rememberNavController()
                NavAppHost(navController)
            }
        }

    }
}

fun List<Film>.getFilmById(id: Int): Int {
    for (f in 0 until this.size)
        if (this[f].id == id) return f
    return -1
}

@Composable
fun NavAppHost(navController: NavHostController) {
    val ctx = LocalContext.current
    var films = remember {
        mutableStateListOf<Film>(
            Film(
                "Avatar",
                7.toBigDecimal(),
                1,
                url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/6DfNtptjkIrevq4mDAB0XDXT0NO.jpg"
            ),
            Film(
                "Titanic",
                9.toBigDecimal(),
                6,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg"
            ),
            Film(
                "Harry Potter i Więzień Azkabanu",
                6.4.toBigDecimal(),
                4,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/sR7uC42HHb7qj1ndLBgBXii0quX.jpg"
            ),
            Film(
                "Marry Popins",
                8.5.toBigDecimal(),
                5,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/uTVGku4LibMGyKgQvjBtv3OYfAX.jpg"
            ),
            Film(
                "John Wick 4",
                7.8.toBigDecimal(),
                3,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg"
            ),
            Film(
                "Avatar: Istota Wody",
                7.toBigDecimal(),
                2,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/Ajs7ZyNjGMHIiATMygxMkOFeTko.jpg"
            ),
            Film(
                "Hellboy",
                4.5.toBigDecimal(),
                7,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hjblR6bPZR0PvFwztHcEBCjYf7d.jpg"
            ),
            Film(
                "Kapitan Ameryka",
                7.6.toBigDecimal(),
                8,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/lvHr3y3g63hrpXI3pSCLF62tRSZ.jpg"
            ),
            Film(
                "Avengers",
                7.9.toBigDecimal(),
                9,
                "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg"
            )
        )
    }
    NavHost(navController = navController, startDestination = NavDestination.List.route) {
        composable(NavDestination.List.route) { ListScreen(navController = navController, films) }
        composable(NavDestination.DetailsFilm.route) { navBackstackEntry ->
            val filmId = navBackstackEntry.arguments?.getString("filmId")
            FilmChoosen(filmId = filmId, navController = navController) {
                EditFilmFormScreen(
                    navController = navController,
                    intention = Intention.DETAILS,
                    onAccept = {
                        navController.navigate(NavDestination.Edit.createRoute(it.id))
                        /* todo przeniesienie to apletu edycji */
                    },
                    film = films[films.getFilmById(it)]
                )

                DetailFilm(navController = navController, filmId = filmId!!.toInt())
            }
        }
        composable(NavDestination.Add.route) {
            val maxId = films.stream().map { it.id }.toList().max()

            EditFilmFormScreen(navController, Intention.ADD, {
                films.add(it)
                navController.popBackStack()
            }, Film(id = maxId+1))
        }
        composable(NavDestination.Edit.route) {
            val filmId = it.arguments?.getString("filmId")
            FilmChoosen(filmId = filmId, navController = navController) {
                EditFilmFormScreen(
                    navController = navController,
                    intention = Intention.EDIT,
                    onAccept = { film ->
                        val index = films.getFilmById(it)
                        films[index] = film
                        navController.popBackStack()
                    },
                    film = films[films.getFilmById(it)]
                )
            }
        }
    }
}

/**
 * If filmId is null then print Toast to user that not specified.
 * content return the filmId not an index in the list
 */
@Composable
fun FilmChoosen(
    filmId: String?,
    navController: NavHostController,
    content: @Composable (Int) -> Unit
) {
    val ctx = navController.context
    if (filmId == null)
        Toast.makeText(
            ctx,
            ctx.getString(R.string.film_required_message),
            Toast.LENGTH_LONG
        )
            .show()
    else {
        content(filmId.toInt())
    }
}
