package com.example.prmprojekt

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.math.BigDecimal

enum class Intention {
    ADD, EDIT, DETAILS
}

@Composable
fun EditFilmFormScreen(
    navController: NavController,
    intention: Intention /* todo: params to differ the addition, edit and view the entry film */,
    onAccept: (Film) -> Unit,
    film: Film
) {
    val ctx = LocalContext.current
    val enabledTextField = if (intention == Intention.DETAILS) false else true
    val acceptButtonText = when (intention){
        Intention.ADD -> ctx.getString(R.string.button_add)
        Intention.EDIT -> ctx.getString(R.string.button_change)
        Intention.DETAILS -> ctx.getString(R.string.button_edit)
    }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        /*val startTitle = if (intention==Intention.ADD) "" else
        val startRating
        val startPictureLink*/

        var title by remember { mutableStateOf(TextFieldValue(film.nazwa)) }
        var rating by remember { mutableStateOf(TextFieldValue(film.rating.toString())) }
        var pictureLink by remember { mutableStateOf(TextFieldValue(film.url)) }
        Column {

            TextField(
                value = title, onValueChange = {
                    title = it
                },
                label = { Text(text = ctx.getString(R.string.text_input_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                enabled = enabledTextField
            )

            TextField(
                value = rating, onValueChange = {
                    rating = it
                },
                label = { Text(text = ctx.getString(R.string.text_input_rating)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                enabled = enabledTextField
            )


            TextField(
                value = pictureLink, onValueChange = {
                    pictureLink = it
                },
                label = { Text(text = ctx.getString(R.string.picture_link_text_input)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                enabled = enabledTextField
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    film.nazwa = title.text
                    film.rating = rating.text.toBigDecimal()
                    film.url = pictureLink.text
                    onAccept(film)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff64cc30)
                )

            ) {
                Text(acceptButtonText)
            }
            if (intention != Intention.DETAILS)
                Button(
                    onClick = { navController.popBackStack() }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    )
                ) {
                    Text(ctx.getString(R.string.button_cancel))
                }


        }

    }

}

@Composable
fun AcceptCancelButtons() {

}