package com.example.prmprojekt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prmprojekt.ui.theme.PRMProjektTheme

sealed class NavDestination(val route: String) {
    object List:NavDestination("list")
    object Add:NavDestination("add")
    object Edit:NavDestination("edit")
    object DetailsFilm:NavDestination("detailsfilm/{filmId}") {
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


@Composable
fun NavAppHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavDestination.List.route) {
        composable(NavDestination.List.route) { ListScreen(navController = navController) }
        composable(NavDestination.DetailsFilm.route) { navBackstackEntry ->
            //navBackstackEntry.arguments?.getInt("filmId")!!.toInt()

            val filmId = navBackstackEntry.arguments?.getString("filmId")
            val bundle = navBackstackEntry.arguments
            if (filmId == null)
                Toast.makeText(LocalContext.current, "@filmIdRequiredMessahe", Toast.LENGTH_LONG)
                    .show()
            else{
                DetailFilm(navController = navController, filmId = filmId!!.toInt())
            }
        }
    }
}
