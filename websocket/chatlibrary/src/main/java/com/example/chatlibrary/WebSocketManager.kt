package com.example.chatlibrary

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.Response
import okio.ByteString
import android.util.Log
import okhttp3.WebSocketListener as OkHttpWebSocketListener


class WebSocketManager(private val listener: WebSocketListener) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url("wss://echo.websocket.org") // ← или свой echo-сервер
            .build()

        webSocket = client.newWebSocket(request, object : OkHttpWebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "✅ Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "📨 Received: $text")
                if (text == "203 = 0xcb") {
                    listener.onMessageReceived("⚠️ Специальный сигнал получен")
                } else {
                    listener.onMessageReceived(text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocket", "📦 Binary received")
                listener.onMessageReceived("📦 Binary: ${bytes.hex()}")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "💥 Error: ${t.message}")
                listener.onError(t.message ?: "Unknown error")
            }
        })
    }

    fun sendMessage(message: String) {
        Log.d("WebSocket", "➡️ Sending: $message")
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnected")
        client.dispatcher.executorService.shutdown()
    }
}