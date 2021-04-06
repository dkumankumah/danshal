package com.example.danshal.ui.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.ItemEventBinding
import com.example.danshal.databinding.ItemGiveawayBinding
import com.example.danshal.databinding.ItemPostBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import com.example.danshal.models.GiveAway
import com.example.danshal.models.Post
import java.text.DateFormatSymbols
import java.util.*

private const val TYPE_EVENT = 0
private const val TYPE_POST = 1
private const val TYPE_GIVEAWAY = 2

class AdminContentAdapter(var contentItems: List<Content>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return when (viewType) {
            TYPE_EVENT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
                EventViewHolder(view)
            }
            TYPE_GIVEAWAY -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_giveaway, parent, false)
                GiveAwayViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
                PostViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == TYPE_EVENT -> {
                (holder as EventViewHolder).bind(contentItems[position] as Event, context)
            }
            getItemViewType(position) == TYPE_GIVEAWAY -> {
                (holder as GiveAwayViewHolder).bind(contentItems[position] as GiveAway, context)
            }
            else -> (holder as PostViewHolder).bind(contentItems[position] as Post, context)
        }
    }

    override fun getItemCount(): Int {
        return contentItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (contentItems[position].postType) {
            Content.TYPE.POST -> TYPE_POST
            Content.TYPE.GIVEAWAY -> TYPE_GIVEAWAY
            else -> TYPE_EVENT
        }
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemEventBinding.bind(itemView)
        val monthName = DateFormatSymbols(Locale.ENGLISH).shortMonths

        fun bind(content: Event, context: Context) {

            binding.tvEventTitle.text = content.title
            binding.tvDay.text = getDate(content.date, false).toString()
            binding.tvMonth.text = monthName[getDate(content.date, true)]

            if (content.imageUrl != null && content.imageUrl != "") {
                Glide.with(context).load(content.imageUrl).into(binding.ivEventImage)
            } else {
                binding.ivEventImage.setImageResource(R.drawable.event2)
            }
        }

        private fun getDate(date: Date, type: Boolean): Int {
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            //return the month (starts at 0) or day
            return if (type) cal.get(Calendar.MONTH) else cal.get(Calendar.DAY_OF_MONTH)
        }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    class GiveAwayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGiveawayBinding.bind(itemView)

        fun bind(content: GiveAway, context: Context) {
            binding.tvGiveAwayTitle.text = content.title
            if (content.imageUrl != null && content.imageUrl != "") {
                Glide.with(context).load(content.imageUrl).into(binding.ivGiveAwayImage)
            } else {
                binding.ivGiveAwayImage.setImageResource(R.drawable.event1)
            }
        }
    }


}