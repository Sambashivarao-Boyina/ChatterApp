package com.example.chatterapp.data.repository

import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Response

class ChatterRepositoryImpl(
    private val chatterApi: ChatterApi
) : ChatterRepository {
    override suspend fun loginUser(user: LoginUser): Response<AuthResponse> {
        return chatterApi.loginUser(user)
    }

    override suspend fun signupUser(user: SignUpUser): Response<AuthResponse> {
        return chatterApi.signupUser(user)
    }
}