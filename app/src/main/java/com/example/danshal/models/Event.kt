package com.example.danshal.models

import com.google.firebase.Timestamp
import java.util.*

data class Event (
    var title: String,
    var content: String,
    var address: Address,
    var date: Date,
    var exclusive: Boolean,
    var imageUrl: String? = null,
    val timestamp: Timestamp? = Timestamp.now()
) {
    companion object {
        val EVENT_EXAMPLES = arrayOf(
            Event("Title event 1", "Content voor event 1", Address(1, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 5), false, "https://firebasestorage.googleapis.com/v0/b/danshal-c7e70.appspot.com/o/content_images%2Fevent-dTgrSiJxoX5imtc5ZlQe.jpg?alt=media&token=f751965d-bf53-425b-ae1b-11681f81dcd5"),
            Event("Title event 2", "Content voor event 2", Address(21, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 21), true, "https://firebasestorage.googleapis.com/v0/b/danshal-c7e70.appspot.com/o/content_images%2Fevent-dTgrSiJxoX5imtc5ZlQe.jpg?alt=media&token=f751965d-bf53-425b-ae1b-11681f81dcd5"),
            Event("Title event 4", "Content voor event 3", Address(33, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 3, 28), false),
            Event("Title event 4", "Content voor event 4", Address(17, null,"1091 GR","Wibautstraat", "Amsterdam"),  Date(2021, 4, 8), true),
        )
    }
}