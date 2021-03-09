package com.example.danshal.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminAddViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is admin add Fragment"
    }
    val text: LiveData<String> = _text
}