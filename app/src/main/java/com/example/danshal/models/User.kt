package com.example.danshal.models

data class User(
    val naam: String = "",
    val address: Address? = null,
    val email: String = "",
    val profileImage: String? = "",
    val userId: String = "",
    val admin: Boolean = false
)
