package com.example.danshal.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.danshal.R
import com.example.danshal.databinding.ItemUserBinding
import com.example.danshal.models.User

class AdminUserAdapter(private val users: List<User>, private val onClick: (User) -> Unit) : RecyclerView.Adapter<AdminUserAdapter.ViewHolder> () {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onClick(users[adapterPosition]) }
        }

        val binding = ItemUserBinding.bind(itemView)

        fun databind(user: User) {
            binding.tvItemName.text = user.naam
            binding.tvItemEmail.text = user.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}