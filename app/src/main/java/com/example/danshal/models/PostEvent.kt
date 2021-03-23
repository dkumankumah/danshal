package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.google.firebase.Timestamp
import java.util.*

// Base class
abstract class PostEvent(
    var postType: String,
    var title: String,
    var content: String,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()) {

    class TYPE {
        companion object {
            val EVENT = "event"
            val POST = "post"
            val GIVEAWAY = "giveaway"
        }
    }
}

data class EventTest(
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
) : PostEvent(PostEvent.TYPE.EVENT, "", "", "")

data class PostTest(
    var exclusive: Boolean,
) : PostEvent(PostEvent.TYPE.POST, "", "", "")

data class GiveAwayTest(
    var participants: List<User>?,
    var endDate: Date,
) : PostEvent(PostEvent.TYPE.GIVEAWAY, "", "", "")
