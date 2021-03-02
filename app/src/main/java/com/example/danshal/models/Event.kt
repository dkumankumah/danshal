package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import java.time.LocalDate
import java.util.*

data class Event (
    var id: Long,
    var title: String,
    var content: String,
    var date: LocalDate,
    @DrawableRes var image: Int,
) {
    companion object {
        val EVENT_EXAMPLES = arrayOf(
            Event(1, "Title event 1", "Content voor event 1", LocalDate.parse("2021.03.09"), R.drawable.event1),
//            Event(2, "Title event 2", "Content voor event 2", R.drawable.event2),
//            Event(3, "Title event 4", "Content voor event 3", R.drawable.event3),
//            Event(4, "Title event 4", "Content voor event 4", R.drawable.event4),
        )
    }
}