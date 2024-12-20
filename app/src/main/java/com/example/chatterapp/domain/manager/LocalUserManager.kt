package com.example.chatterapp.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()

    fun readAppEntry():Flow<Boolean>

    suspend fun saveUserAuth()

    fun readUserAuth(): Flow<Boolean>

    suspend fun saveUserTokne(token: String)

    fun readUserToken(): Flow<String>

    fun readUserEntryAndAuth(): Flow<Pair<Boolean,Boolean>>
}