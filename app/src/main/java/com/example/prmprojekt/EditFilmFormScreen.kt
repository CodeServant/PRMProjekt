package com.example.prmprojekt

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class Intention {
    ADD, EDIT, DETAILS
}

@Composable
fun EditFilmFormScreen(
    navController: NavController,
    intention: Intention,
    onAccept: (Film) -> Unit,
    film: Film
) {
    val ctx = LocalContext.current
    val enabledTextField = if (intention == Intention.DETAILS) false else true
    val acceptButtonText = when (intention) {
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
        var title by remember { mutableStateOf(TextFieldValue(film.nazwa)) }
        var rating by remember {
            val rat = if (film.rating == null) "" else film.rating.toString()
            mutableStateOf(TextFieldValue(rat))
        }
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

                    if (isRating(it.text.toBigDecimalOrNull()) || it.text.isBlank())
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
            else {
                Button(
                    onClick = {
                        var intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","",null))


                        intent.putExtra("sms_body", "${ctx.getString(R.string.text_input_title)}: ${film.nazwa}, ${ctx.getString(R.string.text_input_rating)}: ${film.rating}")
                        ctx.startActivity(intent)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Cyan
                    )
                ) {
                    Row() {
                        Text(ctx.getString(R.string.button_share))
                        Icon(Icons.Default.Share, contentDescription = null)
                    }

                }
            }

        }

    }

}