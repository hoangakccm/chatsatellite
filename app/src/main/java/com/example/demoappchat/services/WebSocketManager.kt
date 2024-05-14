package com.example.demoappchat

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

class WebSocketManager {

    private var socket: Socket? = null

    fun connectToWebSocket() {
        try {
            val opts = IO.Options().apply {
                reconnection = true
                timeout = 3000
                query = "5"
            }
            socket = IO.socket("https://nhatkydientu.vn:80", opts)

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("WebSocketManager", "Connect error: ${args[0]}")
            }
            socket?.on(Socket.EVENT_CONNECT_TIMEOUT) { args ->
                Log.e("WebSocketManager", "Connect timeout: ${args[0]}")
            }

            // Register event handlers
            socket?.on(Socket.EVENT_CONNECT, onConnect)
            socket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket?.on("login_success", onLoginSuccess)

            socket?.connect()
        } catch (e: Exception) {
            Log.e("WebSocketManager", "3 Error connecting to WebSocket: ${e.message}")
        }
    }

    fun sendMessage(message: String) {
        socket?.emit("your_event_name", message)
    }

    fun disconnectWebSocket() {
        socket?.disconnect()
    }

    private val onConnect = Emitter.Listener {
        Log.d("WebSocketManager", "1 Connected to server")
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("WebSocketManager", "2 Disconnected from server")
    }

    private val onLoginSuccess = Emitter.Listener { args ->
        // Handle login success event
        val data = args[0] as? JSONObject
        Log.d("WebSocketManager", "4 Login success: $data")
        // Process the login success data here
    }
}
