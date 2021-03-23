package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import com.google.firebase.Timestamp
import java.util.*

data class Event (
    var title: String = "",
    var content: String = "",
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
    @DrawableRes var image: Int = 0,
    val timestamp: Timestamp? = Timestamp.now()
) {
    constructor() : this("", "",
        Address(0, "", "", "", ""),Date(), false, 0)
}
