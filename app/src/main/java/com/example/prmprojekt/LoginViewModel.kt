package com.example.prmprojekt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    /*val loadingState = MutableStateFlow()*/
    fun signIn(email: String, password: String, setMessage: (String) -> Unit, onSuccess: ()->Unit) =

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "signed to account ${task.result.toString()}")
                            onSuccess()
                        }
                        else
                            try{
                                throw task.exception!!
                            }catch (e:Exception){
                                setMessage(e.message!!)
                            }
                    }

            } catch (e: Exception) {
                setMessage(e.message!!)
            }
        }
    fun signUp(email: String, password: String, setMessage: (String) -> Unit, onSuccess: ()->Unit) =
        viewModelScope.launch {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        Log.d("FB", "signed in an account ${task.result.toString()}")
                        onSuccess()
                    }else{
                        try {
                            throw task.exception!!
                        }catch (e: Exception){
                            setMessage(e.message!!)
                        }
                    }

                }
        }
}