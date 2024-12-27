package com.example.chatterapp.data.remote

import com.example.chatterapp.data.remote.Dto.LoginUser
import com.example.chatterapp.data.remote.Dto.SendedData
import com.example.chatterapp.data.remote.Dto.SignUpUser
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.model.Chat
import com.example.chatterapp.domain.model.Friend
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.domain.model.User
import com.example.chatterapp.domain.model.UserDetails
import com.example.chatterapp.presentation.authetication.components.AuthResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChatterApi {
    @POST("auth/login")
    suspend fun loginUser(
        @Body user: LoginUser
    ): Response<AuthResponse>

    @POST("auth/signup")
    suspend fun signupUser(
        @Body user: SignUpUser
    ): Response<AuthResponse>




    @GET("user")
    suspend fun getAllUser(): Response<List<User>>

    //search User
    @GET("user/search/{searchValue}")
    suspend fun searchUser(@Path("searchValue") searchValue: String): Response<List<User>>

    //sendingRequest
    @POST("request/{id}")
    suspend fun sendFriendRequest(@Path("id") id: String) : Response<ResponseBody>


    //getting self user details
    @GET("user/details")
    suspend fun getSelfDetails(): Response<UserDetails>


    @PATCH("user/about")
    suspend fun updateUserAbout(
        @Body data:UpdateData
    ): Response<ResponseBody>

    @GET("request/sended")
    suspend fun userSendedRequest(): Response<List<FriendRequest>>

    @GET("request/received")
    suspend fun userReceivedRequest(): Response<List<FriendRequest>>

    //Accept Request
    @PATCH("request/accept/{id}")
    suspend fun acceptRequest(@Path("id") id: String) : Response<ResponseBody>

    //Reject Request
    @PATCH("request/reject/{id}")
    suspend fun rejectRequest(@Path("id") id: String) : Response<ResponseBody>

    @GET("user/{id}")
    suspend fun getUserDetails(@Path("id") id: String) : Response<User>

    //image upload
    @Multipart
    @PATCH("user/updateProfile")
    suspend fun updateUserProfile(@Part file: MultipartBody.Part): Response<ResponseBody>



    //chat
    @GET("friend")
    suspend fun getFriendList(): Response<List<Friend>>

    @GET("friend/{id}")
    suspend fun getFriend(@Path("id") id: String): Response<Friend>

    @GET("friend/chat/{id}")
    suspend fun getFriendChat(@Path("id") id: String): Response<Chat>

    @POST("friend/{id}/send")
    suspend fun sendMessage(@Path("id") id: String,@Body sendedData: SendedData) : Response<ResponseBody>

}