package com.example.danshal.models

data class Address (
    var id: Long,
    var housenumber: Int,
    var housenumberExtension: String? = null,
    var postcode: String,
    var street: String,
    var place: String
)