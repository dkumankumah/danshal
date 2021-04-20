package com.example.danshal

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainViewModel(application: Application): AndroidViewModel(application) {

    var auth: FirebaseAuth = Firebase.auth

    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isLoggedIn: LiveData<Boolean>
    get() = _isLoggedIn

    fun checkLoggedIn() {
        _isLoggedIn.value = auth.currentUser != null
        Log.d("check", _isLoggedIn.value.toString())
    }

}