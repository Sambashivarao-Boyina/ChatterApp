package com.example.chatterapp.domain.model

data class Chat(
    val __v: Int,
    val _id: String,
    val blockedBy: Any,
    val messages: List<Message>
)