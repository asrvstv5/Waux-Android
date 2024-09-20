package com.example.waux.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketClient {
    private lateinit var socket: Socket

    init {
        try {
            // Establish Socket.IO connection
            socket = IO.socket("http://10.0.2.2:5000")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun connect(sessionId: String, userId: String) {
        socket.connect()

        // Listen for server events
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("WebSocketClient", "Connected to the server")

            // Join session event
            val data = JSONObject()
            data.put("session_id", sessionId)
            data.put("user_id", userId)

            socket.emit("join_session", data)
        }

        socket.on("join_success") { args ->
            val response = args[0] as JSONObject
            Log.d("WebSocketClient", "Received: ${response.getString("message")}")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("WebSocketClient", "Disconnected from the server")
        }
    }

    // Disconnect function to close the connection and leave the session
    fun disconnect() {
        if (socket.connected()) {
            Log.d("WebSocketClient", "Disconnecting from the server")
            socket.disconnect()
        } else {
            Log.d("WebSocketClient", "Socket is already disconnected")
        }
    }
}
