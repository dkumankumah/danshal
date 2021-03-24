package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

// Base class
abstract class Content(
    var postType: String,
    var title: String,
    var content: String,
    var imageUrl: String? = null,
    var timestamp: Timestamp? = Timestamp.now()) {

    class TYPE {
        companion object {
            val EVENT = "EVENT"
            val POST = "POST"
            val GIVEAWAY = "GIVEAWAY"
        }
    }
}

data class Event(
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
) : Content(TYPE.EVENT, "", "", "")

data class Post(
    var exclusive: Boolean = false,
) : Content(TYPE.POST, "", "", "")

data class GiveAway(
    var participants: List<User>?,
    var endDate: Date,
) : Content(TYPE.GIVEAWAY, "", "", "")