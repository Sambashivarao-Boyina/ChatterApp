package com.example.chatterapp.domain.model

data class Message(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val message: String,
    val sender: String
)