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

enum class Intention {
    ADD, EDIT
}

@Composable
fun EditFilmFormScreen(
    navController: NavController,
    intention: Intention /* todo: params to differ the addition, edit and view the entry film */
) {
    val ctx = LocalContext.current
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween){
        Column {
            var title by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = title, onValueChange = {
                title = it
            },
                label = { Text(text = ctx.getString(R.string.text_input_title)) },
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            )
            var rating by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = rating, onValueChange = {
                rating = it
            },
                label = { Text(text = ctx.getString(R.string.text_input_rating)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            )

            var pictureLink by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = pictureLink, onValueChange = {
                pictureLink = it
            },
                label = { Text(text = ctx.getString(R.string.picture_link_text_input)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            )
        }

        Row (modifier = Modifier.fillMaxWidth()){
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff64cc30)
                )

            ) {
                Text(ctx.getString(R.string.button_add))
            }
            Button(onClick = { navController.popBackStack() }, modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red
                )) {
                Text(ctx.getString(R.string.button_cancel))
            }
        }

    }

}