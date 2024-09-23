package com.example.waux.network

import com.example.waux.network.models.CreateSessionRequest
import com.example.waux.network.models.CreateSessionResponse
import com.example.waux.network.models.JoinSessionRequest
import com.example.waux.network.models.JoinSessionResponse
import com.example.waux.network.models.LoginRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import com.example.waux.network.models.LoginResponse
import com.example.waux.network.models.TokenCheckResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Query("guestUser") guestUser: String, // true or false
        @Body request: LoginRequest
    ): LoginResponse

    @POST("joinSession")
    suspend fun joinSession(
        @Header("Authorization") token: String,
        @Body request: JoinSessionRequest
    ): JoinSessionResponse

    @POST("session")
    suspend fun createSession(
        @Header("Authorization") token: String,
        @Body request: CreateSessionRequest
    ): CreateSessionResponse

    @GET("session")
    suspend fun getSession(
        @Header("Authorization") token: String,
        @Query("session_id") sessionId: String
    ): JoinSessionResponse

    @GET("tokenCheck")
    suspend fun tokenCheck(
        @Header("Authorization") token: String,
    ): TokenCheckResponse
}