package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R
import java.util.*

data class GiveAway (
    var id: Long,
    var title: String,
    var content: String,
    var users: List<User>?,
    var endDate: Date,
    @DrawableRes var image: Int,
) {
    companion object {
        val GIVEAWAY_EXAMPLES = arrayOf(
            GiveAway(1, "test", "test", emptyList(), Date(2021, 3, 20), R.drawable.event1),
            GiveAway(2, "test2", "test2", emptyList(), Date(2021, 3, 20), R.drawable.event2),
        )
    }
}