package com.example.danshal.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminAddPostViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is admin add post Fragment"
    }
    val text: LiveData<String> = _text
}