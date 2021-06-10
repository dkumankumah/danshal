package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

// Base class
abstract class Content(
    var postType: String,
    var id: String,
    var title: String,
    var content: String,
    var imageUrl: String? = null,
    var timestamp: Timestamp? = Timestamp.now()
) {
    fun getSeconds() = timestamp?.seconds

    companion object {
        fun getDate(date: Date, type: Boolean): Int {
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            //return the month (starts at 0) or day
            return if(type) cal.get(Calendar.MONTH) else cal.get(Calendar.DAY_OF_MONTH)
        }
    }

    class TYPE {
        companion object {
            val EVENT = "Event"
            val POST = "Post"
            val GIVEAWAY = "Giveaway"
        }
    }
}

data class Event(
    var address: Address = Address(0, "", "", "", ""),
    var date: Date = Date(),
    var exclusive: Boolean = false,
    var ticket: String = ""
) : Content(TYPE.EVENT, "", "", "", "")

data class Post(
    var exclusive: Boolean = false,
) : Content(TYPE.POST, "", "", "", "")

data class GiveAway(
    var participants: List<String>? = emptyList(),
    var endDate: Date = Date(),
) : Content(TYPE.GIVEAWAY, "", "", "", "")
