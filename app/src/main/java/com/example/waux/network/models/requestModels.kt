package com.example.waux.network.models
import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class LoginRequest(
    val email: String = "",
    val password: String = ""
)

data class JoinSessionRequest(
    @SerializedName("session_id") val sessionId: String = ""
)

data class CreateSessionRequest(
    val name: String = ""
)

@Serializable
data class JoinSessionWebsocketRequest(
    @SerialName("session_id") val sessionId: String = "",
    @SerialName("user_id") val userId: String = ""
)