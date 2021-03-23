package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

data class Event (
    var title: String = "",
    var content: String = "",
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()
) {
    constructor() : this("", "",
        Address(0, "", "", "", ""),Date(), false, null)
}
