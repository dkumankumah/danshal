package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.google.firebase.Timestamp
import java.util.*

data class GiveAway (
    var title: String,
    var content: String,
    var participants: List<User>?,
    var endDate: Date,
    @DrawableRes var image: Int,
    val timestamp: Timestamp? = Timestamp.now()
)