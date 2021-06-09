package com.example.danshal.ui.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.danshal.repository.UserRepository
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
}