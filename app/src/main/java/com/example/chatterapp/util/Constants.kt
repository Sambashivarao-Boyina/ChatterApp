package com.example.chatterapp.util

import okhttp3.ResponseBody
import org.json.JSONObject

object Constants {
    const val USER_SETTINGS = "userSettings"
    const val APP_ENTRY = "appEntry"
    const val USER_AUTH = "userAuth"
    const val USER_TOKEN = "userToken"

    const val USER_ID = "user_id"
    const val FRIEND_ID = "chat_id"


    const val BASE_URL = "http://10.0.2.2:3000/api/"

    // Helper function to extract token from the response body
     fun extractData(responseBody: ResponseBody?, element:String): String? {
        responseBody?.let {
            val responseJson = it.string()
            // Parse the JSON and extract token
            val jsonObject = JSONObject(responseJson)
            return jsonObject.optString(element, null)
        }
        return null
    }
}