package com.example.chatterapp.domain.model

data class UserDetails(
    val _id: String,
    val about: String,
    val email: String,
    val friends: List<String>,
    val receivedRequests: List<String>,
    val sendRequests: List<String>,
    val username: String,
    val userProfile: String? = null,
)