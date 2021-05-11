package com.example.danshal

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SharedUserViewModel(application: Application): AndroidViewModel(application) {

    var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isLoggedIn: LiveData<Boolean>
    get() = _isLoggedIn

    private val _currentUser: MutableLiveData<User> = MutableLiveData()
    val currentUser: LiveData<User>
    get() = _currentUser

    fun checkLoggedIn() {
        _isLoggedIn.value = auth.currentUser != null
        if(auth.currentUser != null){
            retrieveUser(auth.currentUser!!.uid)
        }
        else _currentUser.value = User("", null, "", "", "",false)
    }

    private fun retrieveUser(userId: String) {
        val docRef = db.collection("users").whereEqualTo("userId", userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("clouddata fetching", "DocumentSnapshot data: ${document.toObjects(User::class.java)}")
                    _currentUser.value = document.toObjects(User::class.java)[0]
                } else {
                    Log.d("clouddata fething", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("clouddata fething", "get failed with ", exception)
            }
    }

}