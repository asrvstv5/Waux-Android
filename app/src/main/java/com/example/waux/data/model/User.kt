package com.example.waux.data.model

data class User(
    val userId: String = "",
    val username: String = "",
    val email: String? = null,
    val isGuestUser: String = "true"
)