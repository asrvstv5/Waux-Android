package com.example.waux.network

import android.util.Log
import com.example.waux.network.models.JoinSessionWebsocketRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.client.features.websocket.wss
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

interface WebSocketListener {
    fun onConnected()
    fun onMessage(message: String)
    fun onDisconnected()
}

class WebSocketClient(private val url: String) {

    // Use CIO engine
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    fun connect(listener: WebSocketListener) {
        GlobalScope.launch {
            client.wss(url) {
                listener.onConnected()

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            listener.onMessage(frame.readText())
                        }
                    }
                } catch (e: Exception) {
                    listener.onDisconnected()
                }
            }
        }
    }

    // Connect and send the join_session event
    suspend fun joinSession(sessionId: String, userId: String) {
        client.webSocket(urlString = url) {
            Log.d("WebsocketClient", "WebSocket connection opened")

            val data = JoinSessionWebsocketRequest(sessionId, userId)
            val jsonMessage = buildJsonObject {
                put("event", "join_session")
                putJsonObject("data") {
                    put("session_id", data.sessionId)
                    put("user_id", data.userId)
                }
            }

            val message = jsonMessage.toString()

            Log.d("WebsocketClient", "sending message: $message")
            send(Frame.Text(message))

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        Log.d("WebsocketClient", "received response: ${frame.readText()}")
                    }

                    is Frame.Binary -> TODO()
                    is Frame.Close -> TODO()
                    is Frame.Ping -> TODO()
                    is Frame.Pong -> TODO()
                }
            }
        }
    }


    fun disconnect() {
        client.close()
    }
}