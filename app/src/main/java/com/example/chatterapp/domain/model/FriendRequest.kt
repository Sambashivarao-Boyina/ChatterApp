package com.example.chatterapp.domain.model

data class FriendRequest(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val receiver: User,
    val sender: User,
    val status: String
)