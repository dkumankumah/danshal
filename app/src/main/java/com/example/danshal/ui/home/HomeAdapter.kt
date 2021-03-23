package com.example.danshal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.models.Event


class HomeAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    // Inner class for binding
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Binding to the item_event.xml
        val binding = ItemEventBinding.bind(itemView)

        fun databind(event: Event) {
            binding.tvEventTitle.text = event.title
            binding.ibEventLike.setImageResource(R.drawable.ic_like_true)
            //TODO wanneer image getten werkt, dan dit weg commenten
            // Glide.with(context).load(event.image).into(binding.ivEventImage)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

}