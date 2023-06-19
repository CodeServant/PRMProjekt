package com.example.prmprojekt.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    /*val loadingState = MutableStateFlow()*/

    fun signIn(email: String, password: String) = viewModelScope.launch {

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Log.d("FB", "signed to account ${task.result.toString()}")
                    }else
                    {
                        Log.d("FB", "not signed ${task.result.toString()}")
                    }
                }


    }
}