package com.example.websocket

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.chatlibrary.ChatLibrary // 💥 ВАЖНО: правильный импорт

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this).apply {
            text = "Открыть чат"
            setOnClickListener {
                ChatLibrary.start(this@MainActivity) // 🎯 Вот так вызываем
            }
        }

        setContentView(button)
    }
}