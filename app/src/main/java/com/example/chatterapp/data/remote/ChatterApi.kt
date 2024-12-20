package com.example.chatterapp.data.remote

import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatterApi {
    @POST("auth/login")
    suspend fun loginUser(
        @Body user: LoginUser
    ): Response<AuthResponse>

    @POST("auth/signup")
    suspend fun signupUser(
        @Body user: SignUpUser
    ): Response<AuthResponse>
}