package com.example.danshal.models

data class Address (
    var housenumber: Int?,
    var housenumberExtension: String? = null,
    var postcode: String,
    var street: String,
    var place: String
){
    constructor() : this(null, null, "", "", "")

}