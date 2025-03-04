package com.example.labwork

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.labwork.services.AirplaneModeReceiver

class AirplaneModeFragment : Fragment(R.layout.fragment_airplane_mode) {

    private var modeReceiver: BroadcastReceiver? = null
    private var modeStatusText: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modeStatusText = view.findViewById(R.id.textView_status)

        modeReceiver = AirplaneModeReceiver { isActivated ->
            modeStatusText?.text = if (isActivated) {
                "Airplane Mode: Enabled"
            } else {
                "Airplane Mode: Disabled"
            }
        }
        requireActivity().registerReceiver(modeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        modeReceiver?.let { requireActivity().unregisterReceiver(it) }
        modeStatusText = null
    }
}
