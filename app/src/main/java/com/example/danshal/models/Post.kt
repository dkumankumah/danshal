package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R

data class Post (
    var id: Long,
    var title: String,
    var content: String,
    var exclusive: Boolean,
    @DrawableRes var image: Int,
){
    companion object {
        val POST_EXAMPLES = arrayOf(
            Post(1, "test", "test", false, R.drawable.ic_baseline_admin_panel_settings),
            Post(2, "test2", "test2", true, R.drawable.ic_baseline_admin_panel_settings)
        )
    }
}