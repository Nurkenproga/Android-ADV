package com.example.labwork.services


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labwork.R

data class CalendarEvent(val title: String, val startTime: Long)

class CalendarEventsAdapter(private val events: List<CalendarEvent>) :
    RecyclerView.Adapter<CalendarEventsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textView_event_title)
        val startTimeTextView: TextView = view.findViewById(R.id.textView_event_start_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.title
        holder.startTimeTextView.text = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", event.startTime)
    }

    override fun getItemCount() = events.size
}