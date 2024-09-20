package com.example.waux.network.models

import com.example.waux.data.model.Playlist
import com.example.waux.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    val token: String,
    @SerializedName("user_id") val userId: String,
    val username: String
)

data class JoinSessionResponse(
    val message: String,
    @SerializedName("session_id") val sessionId: String,
    val host: User,
    val name: String,
    val users: List<String>,
    val playlist: Playlist
)

data class CreateSessionResponse(
    val message: String,
    @SerializedName("session_id") val sessionId: String,
    val host: String
)