package com.example.danshal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.danshal.R
import com.example.danshal.databinding.ItemUpEventBinding
import com.example.danshal.models.Event

class UpEventAdapter(private val events: List<Event>): RecyclerView.Adapter<UpEventAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding = ItemUpEventBinding.bind(itemView)

        fun databind(event: Event) {
            binding.tvEventUpTitle.text = event.title
            binding.tvEventUpAddress.text = event.address.place
        }
    }

    // Checks whether a event should be classed as upcoming
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkUpcomingEvent(dateEvent: Date): Boolean {
//        val calendar = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//        val convertEvent = dateEvent.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//        val daysBetween = ChronoUnit.DAYS.between(calendar, convertEvent)
//
//        return daysBetween <= 7 // maand vragen
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_up_event, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }
}