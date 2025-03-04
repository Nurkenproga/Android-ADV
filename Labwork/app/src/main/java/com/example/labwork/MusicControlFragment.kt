package com.example.labwork

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.labwork.services.MusicService

class MusicControlFragment : Fragment(R.layout.fragment_music_control) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton: Button = view.findViewById(R.id.button_start)
        val pauseButton: Button = view.findViewById(R.id.button_pause)
        val stopButton: Button = view.findViewById(R.id.button_stop)

        playButton.setOnClickListener { sendMusicCommand(MusicService.ACTION_START) }
        pauseButton.setOnClickListener { sendMusicCommand(MusicService.ACTION_PAUSE) }
        stopButton.setOnClickListener { sendMusicCommand(MusicService.ACTION_STOP) }
    }

    private fun sendMusicCommand(command: String) {
        Intent(requireActivity(), MusicService::class.java).also { intent ->
            intent.action = command
            requireActivity().startService(intent)
        }
    }
}
