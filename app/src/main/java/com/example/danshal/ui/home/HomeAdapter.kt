package com.example.danshal.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.databinding.ItemPostBinding
import com.example.danshal.models.Content

// Our data structure types
private const val TYPE_EVENT = 0
private const val TYPE_POST = 1


class HomeAdapter(var contentItems: List<Content>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return if(viewType == TYPE_EVENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
            EventViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == TYPE_EVENT) {
            (holder as EventViewHolder).bind(contentItems[position], context)
        } else {
            (holder as PostViewHolder).bind(contentItems[position], context)
        }
    }

    override fun getItemCount(): Int {
        return contentItems.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemEventBinding.bind(itemView)

        fun bind(content: Content, context: Context) {
            binding.tvEventTitle.text = content.title
            if (content.imageUrl != null) {
                Glide.with(context).load(content.imageUrl).into(binding.ivEventImage)
            }
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPostBinding.bind(itemView)

        fun bind(content: Content, context: Context) {
            binding.tvPostImageTitle.text = content.title
            if (content.imageUrl != null) {
                Glide.with(context).load(content.imageUrl).into(binding.ivPostImage)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("HomeAdapter", contentItems[position].postType.toString())
        return if (contentItems[position].postType == Content.TYPE.POST) {
            TYPE_POST
        } else {
            TYPE_EVENT
        }
    }
}