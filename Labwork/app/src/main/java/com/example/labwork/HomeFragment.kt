package com.example.labwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            listOf(
                R.id.button_task1 to R.id.action_homeFragment_to_task1Fragment,
                R.id.button_task2 to R.id.action_homeFragment_to_task2Fragment,
                R.id.button_task3 to R.id.action_homeFragment_to_task3Fragment,
                R.id.button_task4 to R.id.action_homeFragment_to_task4Fragment
            ).forEach { (buttonId, destination) ->
                findViewById<Button>(buttonId).setOnClickListener {
                    findNavController().navigate(destination)
                }
            }
        }
    }
}
