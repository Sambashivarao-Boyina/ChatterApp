package com.example.chatterapp.domain.model

data class Friend(
    val __v: Int? = null,
    val _id: String,
    val chat: String,
    val createdAt: String,
    val lastSeen: String,
    val person: User,
    val updatedAt: String
)