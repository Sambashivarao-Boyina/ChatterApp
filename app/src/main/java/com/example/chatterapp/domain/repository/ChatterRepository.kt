package com.example.chatterapp.domain.repository

import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.model.FriendRequest
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

    //user sended Request
    suspend fun userSendedRequest(): Response<List<FriendRequest>>

    //user received request
    suspend fun userReceivedRequest(): Response<List<FriendRequest>>

    suspend fun acceptRequest(id: String): Response<ResponseBody>
    suspend fun rejectRequest(id: String): Response<ResponseBody>
}