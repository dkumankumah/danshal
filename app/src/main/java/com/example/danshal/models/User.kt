package com.example.danshal.models

data class User(
    val naam: String,
    val address: Address?,
    val email: String,
    val profileImage: String?,
    val userId: String,
    val admin: Boolean = false
) {
    constructor() : this("", null, "", null, "",false)

}
