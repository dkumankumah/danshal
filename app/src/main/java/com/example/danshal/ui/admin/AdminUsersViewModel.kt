package com.example.danshal.ui.admin

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.danshal.R
import com.example.danshal.models.Address
import com.example.danshal.models.User
import com.example.danshal.repository.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AdminUsersViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is admin users Fragment"
    }
    val text: LiveData<String> = _text

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    private val userRepository = UserRepository()
    val users = userRepository.users

    private val db = Firebase.firestore

    fun getUsers() {
        viewModelScope.launch{
            try {
                userRepository.getAllUsers()
            } catch (ex: UserRepository.UserRetrievalError) {
                val errorMsg = "Retrieve user failed."
                Log.e("AdminUsersViewModel", ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

     fun updateUser(user: User, boolean: Boolean) {
        //Unique id generated by Firebase
        val id = user.userId
        //Data inputfields

        db.collection("users")
            .document(id)
            .update(
                mapOf(
                    "admin" to boolean,
                )
            )
    }
}