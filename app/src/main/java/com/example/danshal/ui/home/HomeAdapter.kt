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
import com.example.danshal.models.PostEvent

// Our data structure types
private const val TYPE_EVENT = 0
private const val TYPE_POST = 1
private const val TYPE_GIVEAWAY = 2


class HomeAdapter(var postEventItems: List<PostEvent>) :
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
            (holder as EventViewHolder).bind(postEventItems[position], context)
        } else {
            (holder as PostViewHolder).bind(postEventItems[position], context)
        }
    }

    override fun getItemCount(): Int {
        return postEventItems.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemEventBinding.bind(itemView)

        fun bind(postEvent: PostEvent, context: Context) {
            println("asasasasas")
            binding.tvEventTitle.text = postEvent.title
            if (postEvent.imageUrl != null) {
                Glide.with(context).load(postEvent.imageUrl).into(binding.ivEventImage)
            }
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPostBinding.bind(itemView)
        fun bind(postEvent: PostEvent, context: Context) {
            println("testttt")
            binding.tvPostImageTitle.text = postEvent.title
            if (postEvent.imageUrl != null) {
                Glide.with(context).load(postEvent.imageUrl).into(binding.ivPostImage)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (postEventItems[position].postType === PostEvent.TYPE.EVENT) {
            TYPE_EVENT
        } else {
            TYPE_POST
        }
    }
}