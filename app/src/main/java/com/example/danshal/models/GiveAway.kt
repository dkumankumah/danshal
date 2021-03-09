package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R

data class GiveAway (
    var id: Long,
    var title: String,
    var content: String,
    @DrawableRes var image: Int,
) {
    companion object {
        val GIVEAWAY_EXAMPLES = arrayOf(
                GiveAway(1, "test", "test", R.drawable.ic_baseline_admin_panel_settings),
                GiveAway(2, "test2", "test2", R.drawable.ic_baseline_admin_panel_settings)
                )
    }
}