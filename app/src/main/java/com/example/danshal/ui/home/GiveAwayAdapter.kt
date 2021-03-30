package com.example.danshal.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.ItemGiveawayBinding
import com.example.danshal.models.GiveAway

class GiveAwayAdapter(var giveAway: List<GiveAway>, private val onClick: (GiveAway) -> Unit) :
    RecyclerView.Adapter<GiveAwayAdapter.ViewHolder>() {

        private lateinit var context: Context

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

            init {
                itemView.setOnClickListener {onClick(giveAway[adapterPosition])}
            }

            private val binding = ItemGiveawayBinding.bind(itemView)

            fun bind(giveAway: GiveAway) {
                binding.tvGiveAwayTitle.text = giveAway.title

                if(giveAway.imageUrl != null && giveAway.imageUrl != "") {
                    Glide.with(context).load(giveAway.imageUrl).into(binding.ivGiveAwayImage)
                } else {
                    binding.ivGiveAwayImage.setImageResource(R.drawable.event1)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiveAwayAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_giveaway, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(giveAway[position])
    }

    override fun getItemCount(): Int {
        return giveAway.size
    }


}