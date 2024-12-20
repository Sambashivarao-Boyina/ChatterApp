package com.example.chatterapp.domain.repository

import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface ChatterRepository {
    suspend fun loginUser(user:LoginUser): Response<AuthResponse>
    suspend fun signupUser(user: SignUpUser):Response<AuthResponse>
}