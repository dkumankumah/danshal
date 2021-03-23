package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

data class GiveAway (
    var title: String,
    var content: String,
    var participants: List<User>?,
    var endDate: Date,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()
)