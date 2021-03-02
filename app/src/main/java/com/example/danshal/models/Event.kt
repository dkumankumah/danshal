package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R

data class Event (
    var id: Long,
    var title: String,
    var content: String,
    @DrawableRes var image: Int,
) {
    companion object {
        val EVENT_EXAMPLES = arrayOf(
            Event(1, "Title event 1", "Content voor event 1", R.drawable.ic_baseline_admin_panel_settings),
            Event(2, "Title event 1", "Content voor event 1", R.drawable.ic_baseline_admin_panel_settings)
        )
    }
}