package com.example.danshal.models

import androidx.annotation.DrawableRes
import com.example.danshal.R

data class Post (
    var title: String,
    var content: String,
    var exclusive: Boolean,
    @DrawableRes var image: Int,
)