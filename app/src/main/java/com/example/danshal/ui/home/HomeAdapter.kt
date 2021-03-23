package com.example.danshal.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.databinding.ItemPostImageBinding
import com.example.danshal.databinding.ItemPostTextBinding
import com.example.danshal.models.Event
import com.example.danshal.models.Post
import java.lang.IllegalArgumentException


class HomeAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.BaseViewHolder<*>>() {
    // *Any* makes sure we can render our desired ViewHolder
    private val adapterDataList: List<Any> = emptyList()

    // Our data structure types
    companion object {
        private const val TYPE_EVENT = 0
        private const val TYPE_POST_TEXT = 1
        private const val TYPE_POST_IMAGE = 2
    }

    // BaseVIewHolder will push different type of view into the recyclerview
    abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    // Inner class for binding
    inner class EventViewHolder(itemView: View) : BaseViewHolder<Event>(itemView) {
        val binding = ItemEventBinding.bind(itemView)

        override fun bind(item: Event) {
            binding.tvEventTitle.text = item.title
            binding.ibEventLike.setImageResource(R.drawable.ic_like_true)
            //wanneer image getten werkt, dan dit weg commenten
            // Glide.with(context).load(event.image).into(binding.ivEventImage)
        }
    }

    inner class PostTextViewHolder(itemView: View): BaseViewHolder<Post>(itemView) {
        val binding = ItemPostTextBinding.bind(itemView)

        override fun bind(item: Post) {
            binding.tvPostTextTitle.text = item.title
            binding.tvPostTextContent.text = item.content
        }
    }


    inner class PostImageViewHolder(itemView: View) : BaseViewHolder<Post>(itemView) {
        val binding = ItemPostImageBinding.bind(itemView)

        override fun bind(item: Post) {
            binding.tvPostImageTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_EVENT -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_event, parent, false)
                EventViewHolder(view)
            }
            TYPE_POST_TEXT -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_post_text, parent, false)
                PostTextViewHolder(view)
            }
            TYPE_POST_IMAGE -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_post_image, parent, false)
                PostImageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = adapterDataList[position]
        when(holder) {
            is EventViewHolder -> holder.bind(element as Event)
            is PostTextViewHolder -> holder.bind(element as Post)
            is PostImageViewHolder -> holder.bind(element as Post)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }
}