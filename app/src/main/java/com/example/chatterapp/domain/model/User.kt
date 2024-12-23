package com.example.chatterapp.domain.model

data class User(
    val _id: String,
    val username: String,
    val email: String,
    val userProfile: String? = null,
    val about: String
)
