package com.example.chatterapp.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.util.Constants
import com.example.chatterapp.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(
    private val context: Context
):LocalUserManager {
    override suspend fun saveAppEntry() {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.datastore.data.map { preferenceKeys ->
            preferenceKeys[PreferenceKeys.APP_ENTRY]?:false
        }
    }

    override suspend fun saveUserAuth() {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.USER_AUTH] = true
        }
    }

    override fun readUserAuth(): Flow<Boolean> {
        return context.datastore.data.map { preferenceKeys ->
            preferenceKeys[PreferenceKeys.USER_AUTH] ?: false
        }
    }

    override suspend fun saveUserTokne(token: String) {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.USER_TOKEN] = token
        }
    }

    override fun readUserToken(): Flow<String> {
        return context.datastore.data.map { preferencesKeys ->
            preferencesKeys[PreferenceKeys.USER_TOKEN] ?: ""
        }
    }

    override fun readUserEntryAndAuth(): Flow<Pair<Boolean, Boolean>> {
        return context.datastore.data.map { preferenceKeys ->
            val userEntry = preferenceKeys[PreferenceKeys.APP_ENTRY] ?: false
            val userAuth = preferenceKeys[PreferenceKeys.USER_AUTH] ?: false

            Pair(userEntry, userAuth)
        }
    }

    override suspend fun logOutUser() {
        context.datastore.edit { settings ->
            settings.remove(PreferenceKeys.USER_AUTH)
            settings.remove(PreferenceKeys.USER_TOKEN)
        }
    }

}

private val Context.datastore: DataStore<Preferences>  by preferencesDataStore(name = USER_SETTINGS)

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
    val USER_AUTH = booleanPreferencesKey(name = Constants.USER_AUTH)
    val USER_TOKEN = stringPreferencesKey(name = Constants.USER_TOKEN)
}