package com.example.danshal.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminAddEventViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is admin add event Fragment"
    }
    val text: LiveData<String> = _text
}