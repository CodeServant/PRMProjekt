package com.example.prmprojekt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prmprojekt.ui.theme.LoginViewModel

@Composable
fun CredentialForm(
    navController: NavController,
    title: String,
    registering: Boolean,
    onAccpted: (String, String) -> Unit,
    loginViewModel: LoginViewModel
) {
    val ctx = navController.context
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    var password2 by remember {
        mutableStateOf("")
    }
    var messageText by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {

        Text(
            text = "$title",
            fontSize = 50.sp,
            modifier = Modifier.weight(1f)
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceAround) {
            InputField(
                labelResId = R.string.email_field,
                inVal = email,
                onValueChange = { email = it },
                isPassword = false,
                placeholderResId = R.string.example_email,
                navController = navController,
            )
            InputField(
                labelResId = R.string.password_field,
                inVal = password,
                onValueChange = {
                    password = it
                },
                isPassword = true,
                navController = navController,
            )
            if (registering)
                InputField(
                    labelResId = R.string.password_field_secound,
                    inVal = password2,
                    onValueChange = {
                        password2 = it
                    },
                    isPassword = true,
                    navController = navController,
                )
        }
        val acceptResMessage =
            if (registering) R.string.button_register_accept else R.string.button_login_accept
        Text(text = messageText)
        AcceptButton(navController = navController, acceptResMessage, {
            if (!password.equals(password2)) messageText = ctx.getString(R.string.passwords_not_equals)
            else
            {
                messageText=""
                onAccpted(email, password)
            }
        })
    }
}

@Composable
fun AcceptButton(navController: NavController, messageResId: Int, onClick: () -> Unit) {
    val ctx = navController.context

    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = ctx.getString(messageResId), fontSize = 30.sp)
    }
}

@Composable
fun InputField(
    navController: NavController,
    labelResId: Int,
    placeholderResId: Int? = null,
    inVal: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean
) {
    val ctx = navController.context
    val keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
    TextField(
        value = inVal,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = ctx.getString(labelResId),
                fontSize = 26.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(fontSize = 35.sp),
        placeholder = {
            Text(text = if (placeholderResId != null) ctx.getString(placeholderResId) else "")
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)

    )
}
