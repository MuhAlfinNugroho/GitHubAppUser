package com.alfin.githubappuser.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class FavoriteAdapter(private val listFavorite: List<FavoriteEntity>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteEntity)
    }

    inner class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUser: TextView = binding.username
        val ivPhoto: ImageView = binding.picture
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUser.text = listFavorite[position].username
        Glide.with(holder.itemView.context)
            .load(listFavorite[position].avatar)
            .into(holder.ivPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.absoluteAdapterPosition])
        }
    }
}