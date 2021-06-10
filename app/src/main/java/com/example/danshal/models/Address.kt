package com.example.danshal.models

data class Address (
    var housenumber: Int = 0,
    var housenumberExtension: String? = null,
    var postcode: String = "",
    var street: String = "",
    var place: String = ""
)