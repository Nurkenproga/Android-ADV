package com.example.labwork

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labwork.services.CalendarEvent
import com.example.labwork.services.CalendarEventsAdapter

class CalendarEventsFragment : Fragment(R.layout.fragment_calendar_events) {

    private var eventList: RecyclerView? = null
    private var eventAdapter: CalendarEventsAdapter? = null
    private val eventData = mutableListOf<CalendarEvent>()

    companion object {
        private const val CALENDAR_PERMISSION_CODE = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventList = view.findViewById(R.id.recyclerView_calendar)
        eventList?.layoutManager = LinearLayoutManager(requireContext())
        eventAdapter = CalendarEventsAdapter(eventData)
        eventList?.adapter = eventAdapter

        requestPermission(
            CALENDAR_PERMISSION_CODE,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR
        )
    }

    private fun requestPermission(requestCode: Int, vararg permissions: String) {
        if (permissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }) {
            loadCalendarEvents()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALENDAR_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents()
        }
    }

    private fun loadCalendarEvents() {
        val columns = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )

        val cursor: Cursor? = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            columns,
            null,
            null,
            "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            val titleIdx = it.getColumnIndex(CalendarContract.Events.TITLE)
            val timeIdx = it.getColumnIndex(CalendarContract.Events.DTSTART)

            while (it.moveToNext()) {
                val title = it.getString(titleIdx)
                val startTime = it.getLong(timeIdx)
                eventData.add(CalendarEvent(title, startTime))
            }
        }

        eventAdapter?.notifyDataSetChanged()
    }
}
