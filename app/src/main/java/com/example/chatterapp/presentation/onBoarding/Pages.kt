package com.example.chatterapp.presentation.onBoarding

import android.icu.text.CaseMap.Title
import com.example.chatterapp.R

data class Page(
    val title: String,
    val description: String,
    val image: Int
)

val pages = listOf(
    Page(
        title = "Stay Connected",
        description = "Message and share instantly, anytime, anywhere.",
        image = R.raw.connect
    ),
    Page(
        title = "Private & Secure",
        description = "Your chats are safe with end-to-end encryption.",
        image = R.raw.secure
    ),
    Page(
        title = "Express Yourself",
        description = "Make conversations lively with fun features!",
        image = R.raw.express
    )
)