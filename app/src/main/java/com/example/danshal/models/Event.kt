package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.util.*

data class Event (
    var title: String,
    var content: String,
    var address: Address,
    var date: Date,
    var exclusive: Boolean,
    @DrawableRes var image: Int,
    val timestamp: Timestamp? = Timestamp.now()
) {
    companion object {
        val EVENT_EXAMPLES = arrayOf(
            Event("Title event 1", "Content voor event 1", Address(1, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 5), false, R.drawable.event1),
            Event("Title event 2", "Content voor event 2", Address(21, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 21), true, R.drawable.event2),
            Event("Title event 4", "Content voor event 3", Address(33, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 28), false, R.drawable.event3),
            Event("Title event 4", "Content voor event 4", Address(17, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 4, 8), true, R.drawable.event4),
        )
    }
}