package com.example.chatterapp.domain.repository

import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface ChatterRepository {
    suspend fun loginUser(user:LoginUser): Response<AuthResponse>
    suspend fun signupUser(user: SignUpUser):Response<AuthResponse>

    //getting the friends list
    suspend fun getFriendsList(): Response<List<Friend>>

    //getting all users list
    suspend fun getAllUsers(): Response<List<User>>

    //sending friend request
    suspend fun sendFriendRequest(id: String): Response<ResponseBody>

    //getting user details
    suspend fun getSelfDetails(): Response<UserDetails>

    suspend fun updateUserAbout(
        updateData: UpdateData
    ): Response<ResponseBody>
}