package com.example.danshal.models

import com.google.firebase.Timestamp

data class Notification (
    var text: String? = "",
    var timestamp: Timestamp? = Timestamp.now()
)