package com.example.chatterapp.di

import android.app.Application
import com.example.chatterapp.data.manager.LocalUserManagerImpl
import com.example.chatterapp.data.remote.ChatterApi
import com.example.chatterapp.data.repository.ChatterRepositoryImpl
import com.example.chatterapp.domain.manager.LocalUserManager
import com.example.chatterapp.domain.repository.ChatterRepository
import com.example.chatterapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ):LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideChatterApi(
        localUserManage : LocalUserManager
    ):ChatterApi {
        val TIMEOUT = 30L // Timeout in seconds

         val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response
        }

        val token = runBlocking {
            localUserManage.readUserToken().firstOrNull() ?: ""
        }

        val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS) // Connection timeout
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)    // Read timeout
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)   // Write timeout
                .addInterceptor(loggingInterceptor)        // Add logging
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer ${token}") // Replace with dynamic JWT token
                        .header("Content-Type", "application/json")
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
        }
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatterRepository(
        chatterApi: ChatterApi
    ): ChatterRepository = ChatterRepositoryImpl(chatterApi = chatterApi)
}