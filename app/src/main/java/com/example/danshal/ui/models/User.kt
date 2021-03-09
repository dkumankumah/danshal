package com.example.danshal.ui.models

data class User(val naam: String,
                val adres: String,
                val postcode: String,
                val plaats: String,
                val email: String,
                val userId: String,
                val isAdmin: Int = 0 )
