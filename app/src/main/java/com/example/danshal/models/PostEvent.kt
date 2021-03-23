package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

// Base class
abstract class PostEvent(
    var postType: Int,
    var title: String,
    var content: String,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()) {

    class TYPE {
        companion object {
            val EVENT = 0
            val POST = 1
            val GIVEAWAY = 2
        }
    }
}

data class EventTest(
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
) : PostEvent(TYPE.EVENT, "", "", "")

data class PostTest(
    var exclusive: Boolean = false,
) : PostEvent(TYPE.POST, "", "", "")

data class GiveAwayTest(
    var participants: List<User>?,
    var endDate: Date,
) : PostEvent(TYPE.GIVEAWAY, "", "", "")
