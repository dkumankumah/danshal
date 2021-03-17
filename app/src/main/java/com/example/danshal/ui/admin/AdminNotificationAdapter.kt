package com.example.danshal.ui.admin

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.danshal.R
import com.example.danshal.databinding.AdminItemNotificationBinding
import com.example.danshal.models.Notification

class AdminNotificationAdapter(private val notifications: List<Notification>): RecyclerView.Adapter<AdminNotificationAdapter.ViewHolder> () {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding = AdminItemNotificationBinding.bind(itemView)

        fun databind(notification: Notification) {
            Log.d("ADMIN NOTIFICATION AD", notification.toString())
            binding.tvNotificationText.text = notification.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminNotificationAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.admin_item_notification, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdminNotificationAdapter.ViewHolder, position: Int) {
        holder.databind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}