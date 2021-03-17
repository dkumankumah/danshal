package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import com.google.firebase.Timestamp

data class Post (
    var title: String,
    var content: String,
    var exclusive: Boolean,
    @DrawableRes var image: Int,
    val timestamp: Timestamp? = Timestamp.now()
)