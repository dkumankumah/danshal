package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import java.time.LocalDate
import java.util.*

data class Event (
    var id: Long,
    var title: String,
    var content: String,
    var address: String,
    var date: Date,
    var exclusive: Boolean,
    @DrawableRes var image: Int,
) {
    companion object {
        val EVENT_EXAMPLES = arrayOf(
            Event(1, "Title event 1", "Content voor event 1", "Amsterdam",  Date(2021, 3, 5), false, R.drawable.event1),
            Event(2, "Title event 2", "Content voor event 2", "Amsterdam",  Date(2021, 3, 21), true, R.drawable.event2),
            Event(3, "Title event 4", "Content voor event 3", "Amsterdam",  Date(2021, 3, 28), false, R.drawable.event3),
            Event(4, "Title event 4", "Content voor event 4", "Amsterdam",  Date(2021, 4, 8), true, R.drawable.event4),
        )
    }
}