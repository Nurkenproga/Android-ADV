package com.example.labwork.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.provider.Settings

class AirplaneModeReceiver(private val onAirplaneModeChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            val message = if (isAirplaneModeOn) {
                "Airplane Mode is ON"
            } else {
                "Airplane Mode is OFF"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onAirplaneModeChanged(isAirplaneModeOn)
        }
    }
}