package com.example.danshal.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.databinding.ItemPostBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import com.example.danshal.models.Post
import java.text.DateFormatSymbols
import java.util.*


// Our data structure types
private const val TYPE_EVENT = 0
private const val TYPE_POST = 1


class HomeAdapter(var contentItems: List<Content>, private val onClick: (Content) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return if (viewType == TYPE_EVENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
            EventViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_EVENT) {
            (holder as EventViewHolder).bind(contentItems[position] as Event, context)
        } else {
            (holder as PostViewHolder).bind(contentItems[position] as Post, context)
        }
    }

    override fun getItemCount(): Int {
        return contentItems.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { onClick(contentItems[adapterPosition]) }
        }

        val binding = ItemEventBinding.bind(itemView)
        val monthName = DateFormatSymbols(Locale.ENGLISH).shortMonths

        fun bind(content: Event, context: Context) {

            binding.tvEventTitle.text = content.title
            binding.tvDay.text = Content.getDate(content.date, false).toString()
            binding.tvMonth.text = monthName[Content.getDate(content.date, true)]

            if (content.imageUrl != null && content.imageUrl != "") {
                Glide.with(context).load(content.imageUrl).into(binding.ivEventImage)
            } else {
                binding.ivEventImage.setImageResource(R.drawable.event2)
            }
        }
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { onClick(contentItems[adapterPosition]) }
        }

        val binding = ItemPostBinding.bind(itemView)

        fun bind(content: Post, context: Context) {
            binding.tvPostImageTitle.text = content.title
            if (content.imageUrl != null && content.imageUrl != "") {
                Glide.with(context).load(content.imageUrl).into(binding.ivPostImage)
            } else {
                binding.ivPostImage.setImageResource(R.drawable.event2)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (contentItems[position].postType == Content.TYPE.POST) {
            TYPE_POST
        } else {
            TYPE_EVENT
        }
    }
}