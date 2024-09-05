package com.example.appcomposeretrofit.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcomposeretrofit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(email: String, password: String, home: ()-> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Log.d("Lui", "Te has logueado!")
                        home()
                    }else{
                        Log.d("Lui", "Error de logueo!: ${task.result.toString()}")
                    }
                }
        }catch (ex:Exception){
            Log.d("Lui", "signInWithEmailAndPassword: ${ex.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }else{
                        Log.d("Lui", "Error al crear usuario: ${task.result.toString()}")
                    }
                    _loading.value = false
                }

        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        //val user = mutableMapOf<String, Any>()

        //user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()
        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarURL = "",
            quote = "Eso es todo :)",
            profession = "Desarrollador Android",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Lui", "Usuario ${it.id} creado.")
            }.addOnFailureListener{
                Log.d("Lui", "Ocurrio un error ${it}.")
            }


    }
}