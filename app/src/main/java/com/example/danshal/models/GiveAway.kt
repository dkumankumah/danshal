package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

data class GiveAway (
    var title: String = "",
    var content: String = "",
    var endDate: Date = Date(),
    var imageUrl: String? = null,
    var participants: List<User>? = emptyList(),
    val timestamp: Timestamp? = Timestamp.now()
)