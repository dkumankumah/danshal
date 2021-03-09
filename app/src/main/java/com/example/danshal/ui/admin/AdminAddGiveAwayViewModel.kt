package com.example.danshal.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminAddGiveAwayViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is admin add give away Fragment"
    }
    val text: LiveData<String> = _text
}