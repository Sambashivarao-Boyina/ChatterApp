package com.example.chatterapp.domain.model

data class Friend(
    val __v: Int? = null,
    val _id: String,
    val chat: String,
    val person: User,
    val lastMessage: Message? = null,
)