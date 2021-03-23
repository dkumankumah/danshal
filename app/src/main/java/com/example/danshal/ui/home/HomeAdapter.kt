package com.example.danshal.ui.home

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.models.Event

class HomeAdapter(private val events: List<Event>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var context: Context

    // Inner class for binding
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // Binding to the item_event.xml
        val binding = ItemEventBinding.bind(itemView)

        fun databind(event: Event) {
            binding.tvEventTitle.text = event.title
            Glide.with(context).load(event.imageUrl).into(binding.ivEventImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        context = vh.context

        return ViewHolder(vh)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

}