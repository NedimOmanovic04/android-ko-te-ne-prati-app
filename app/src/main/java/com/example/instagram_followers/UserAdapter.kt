package com.example.instagram_followers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val users: List<Pair<String, String>>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val btnOpen: LinearLayout = view.findViewById(R.id.tvOpen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, href) = users[position]
        holder.tvUsername.text = "@$username"
        holder.btnOpen.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(href))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = users.size
}