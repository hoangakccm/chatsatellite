package com.example.demoappchat
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager {

    private var webSocket: WebSocket? = null

    fun connectToWebSocket() {
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://nhatkydientu.vn:5000") // Địa chỉ IP của máy chủ WebSocket
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                println("Received message from server: $text")
                // Xử lý tin nhắn từ máy chủ ở đây
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("WebSocket connection failed: ${t.message}")
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnectWebSocket() {
        webSocket?.cancel()
    }
}