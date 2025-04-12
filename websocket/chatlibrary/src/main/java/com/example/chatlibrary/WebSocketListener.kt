package com.example.chatlibrary

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket

interface WebSocketListener {
    fun onMessageReceived(message: String)
    fun onError(error: String)

    fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WS", "Socket открыт: $response")
    }
}