package com.example.chatterapp.data.repository

import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SendedData
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.model.Chat
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.MultipartBody
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

    override suspend fun getFriendsList(): Response<List<Friend>> {
        return chatterApi.getFriendList()
    }

    override suspend fun getAllUsers(): Response<List<User>> {
        return chatterApi.getAllUser()
    }

    override suspend fun searchUsers(searchValue: String): Response<List<User>> {
        return chatterApi.searchUser(searchValue)
    }

    override suspend fun sendFriendRequest(id: String): Response<ResponseBody> {
        return chatterApi.sendFriendRequest(id = id)
    }

    override suspend fun getSelfDetails(): Response<UserDetails> {
        return chatterApi.getSelfDetails()
    }

    override suspend fun updateUserAbout(updateData: UpdateData): Response<ResponseBody> {
        return chatterApi.updateUserAbout(data = updateData)
    }

    override suspend fun userSendedRequest(): Response<List<FriendRequest>> {
        return chatterApi.userSendedRequest()
    }

    override suspend fun userReceivedRequest(): Response<List<FriendRequest>> {
        return chatterApi.userReceivedRequest()
    }

    override suspend fun acceptRequest(id: String): Response<ResponseBody> {
        return chatterApi.acceptRequest(id = id)
    }

    override suspend fun rejectRequest(id: String): Response<ResponseBody> {
        return chatterApi.rejectRequest(id = id)
    }

    override suspend fun getUserDetails(id: String): Response<User> {
        return chatterApi.getUserDetails(id = id)
    }

    override suspend fun updateUserProfile(file: MultipartBody.Part): Response<ResponseBody> {
        return chatterApi.updateUserProfile(file)
    }

    override suspend fun getFriend(id: String): Response<Friend> {
        return chatterApi.getFriend(id = id)
    }

    override suspend fun getFriendChat(id: String): Response<Chat> {
        return chatterApi.getFriendChat(id = id)
    }

    override suspend fun sendMessage(id: String,sendedData: SendedData): Response<ResponseBody> {
        return chatterApi.sendMessage(id = id,sendedData = sendedData)
    }


}