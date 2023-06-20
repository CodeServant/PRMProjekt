package com.example.prmprojekt

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prmprojekt.data.FilmDatabase
import com.example.prmprojekt.data.FilmEntity
import com.example.prmprojekt.data.FilmRepository
import com.example.prmprojekt.ui.theme.PRMProjektTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.streams.toList

sealed class NavDestination(val route: String) {
    object List : NavDestination("list")
    object Add : NavDestination("add")
    object Edit : NavDestination("edit/{filmId}") {
        fun createRoute(filmId: Int) = "edit/$filmId"
    }

    object DetailsFilm : NavDestination("detailsfilm/{filmId}") {
        fun createRoute(filmId: Int) = "detailsfilm/$filmId"
    }

    object RegisterlForm : NavDestination("resgister")
    object LoginForm : NavDestination("login")
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

internal fun List<Film>.getFilmById(id: Int): Int {
    for (f in 0 until this.size)
        if (this[f].id == id) return f
    return -1
}

internal fun entityToFilm(entities: List<FilmEntity>): MutableList<Film> {
    val filmList: MutableList<Film> = mutableListOf()
    entities.forEach {
        filmList.add(entityToFilm(it))
    }
    return filmList
}

internal fun entityToFilm(entity: FilmEntity): Film {
    return Film(
        nazwa = entity.nazwa,
        id = entity.id,
        url = entity.url,
        rating = entity.rating
    )

}

internal fun toFilmEntity(film: Film): FilmEntity {
    return FilmEntity(
        nazwa = film.nazwa,
        id = film.id,
        url = film.url,
        rating = film.rating
    )
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun NavAppHost(navController: NavHostController) {
    val ctx = LocalContext.current

    val databse = FilmDatabase.getintance(ctx)
    val dao = databse.filmDAO
    val repo = FilmRepository(dao)
    var signFeedback by remember {
        mutableStateOf("")
    }

    val films by repo.films.map { entityToFilm(it) }
        .collectAsStateWithLifecycle(initialValue = mutableListOf<Film>())

    val corScope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = NavDestination.LoginForm.route) {
        composable(NavDestination.List.route) {
            ListScreen(navController = navController, films, {
                corScope.launch {
                    repo.delete(toFilmEntity(it))
                }
            }, {
                corScope.launch {
                    dummyfilmArray().forEach {
                        repo.insert(toFilmEntity(it))
                    }
                }
            })
        }
        composable(NavDestination.DetailsFilm.route) { navBackstackEntry ->
            val filmId = navBackstackEntry.arguments?.getString("filmId")
            FilmChoosen(filmId = filmId, navController = navController) {
                EditFilmFormScreen(
                    navController = navController,
                    intention = Intention.DETAILS,
                    onAccept = {
                        navController.navigate(NavDestination.Edit.createRoute(it.id!!))
                    },
                    film = films[films.getFilmById(it)]
                )

                //DetailFilm(navController = navController, filmId = filmId!!.toInt())
            }
        }
        composable(NavDestination.Add.route) {
            val maxId = films.stream().map { it.id!! }.toList().maxOrNull() ?: 1


            EditFilmFormScreen(navController, Intention.ADD, {
                corScope.launch {
                    repo.insert(toFilmEntity(it))
                }
                navController.popBackStack()
            }, Film(id = maxId + 1))
        }
        composable(NavDestination.Edit.route) {
            val filmId = it.arguments?.getString("filmId")
            FilmChoosen(filmId = filmId, navController = navController) {
                EditFilmFormScreen(
                    navController = navController,
                    intention = Intention.EDIT,
                    onAccept = { film ->

                        corScope.launch {
                            repo.update(toFilmEntity(film))
                        }
                        navController.popBackStack()
                    },
                    film = films[films.getFilmById(it)]
                )
            }
        }
        val logViewModel = LoginViewModel()
        composable(NavDestination.RegisterlForm.route) {
            CredentialForm(
                navController = navController,
                ctx.getString(R.string.register_form_title),
                registering = true,
                onAccepted = { email, password ->
                    logViewModel.signUp(
                        email,
                        password,
                        setMessage = { signFeedback = it },
                        onSuccess = {
                            signFeedback = "You are now a user!" //todo set string values
                        })
                },
                logViewModel,
                {
                    navController.navigate(NavDestination.LoginForm.route) {
                        popUpTo(NavDestination.RegisterlForm.route) {
                            inclusive = true
                        }
                    }
                },
                textToRedirect = ctx.getString(R.string.login_form_title),
                messageText = signFeedback,
                setMessage = { signFeedback = it }
            )
        }

        composable(NavDestination.LoginForm.route) {
            CredentialForm(
                navController = navController,
                ctx.getString(R.string.login_form_title),
                registering = false,
                onAccepted = { email, password ->
                    logViewModel.signIn(email, password, setMessage = { signFeedback = it }){
                        navController.navigate(NavDestination.List.route){
                            popUpTo(NavDestination.LoginForm.route){
                                inclusive = true
                            }
                        }


                    }
                },
                logViewModel,
                {
                    navController.navigate(NavDestination.RegisterlForm.route) {
                        popUpTo(NavDestination.LoginForm.route) {
                            inclusive = true
                        }
                    }
                },
                textToRedirect = ctx.getString(R.string.register_form_title),
                messageText = signFeedback,
                setMessage = { signFeedback = it }
            )
        }
    }
}

fun dummyfilmArray(): Array<Film> {

    val list = mutableListOf<Film>(
        Film(
            "Avatar",
            7.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/6DfNtptjkIrevq4mDAB0XDXT0NO.jpg"
        ),
        Film(
            "Titanic",
            9.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg"
        ),
        Film(
            "Harry Potter i Więzień Azkabanu",
            6.4.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/sR7uC42HHb7qj1ndLBgBXii0quX.jpg"
        ),
        Film(
            "Marry Popins",
            8.5.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/uTVGku4LibMGyKgQvjBtv3OYfAX.jpg"
        ),
        Film(
            "John Wick 4",
            7.8.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg"
        ),
        Film(
            "Avatar: Istota Wody",
            7.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/Ajs7ZyNjGMHIiATMygxMkOFeTko.jpg"
        ),
        Film(
            "Hellboy",
            4.5.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hjblR6bPZR0PvFwztHcEBCjYf7d.jpg"
        ),
        Film(
            "Kapitan Ameryka",
            7.6.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/lvHr3y3g63hrpXI3pSCLF62tRSZ.jpg"
        ),
        Film(
            "Avengers",
            7.9.toBigDecimal(),
            null,
            url = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg"
        )
    )
    return list.toTypedArray()
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
