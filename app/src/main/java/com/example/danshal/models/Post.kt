package com.example.danshal.models

import com.google.firebase.Timestamp

data class Post (
    var title: String = "",
    var content: String = "",
    var exclusive: Boolean = false,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()
)