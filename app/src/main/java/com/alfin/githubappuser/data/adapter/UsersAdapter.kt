package com.alfin.githubappuser.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alfin.githubappuser.data.response.ItemsItem
import com.alfin.githubappuser.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private val listUser = ArrayList<ItemsItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(v: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(v.context), v, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    class DIFFCALLBACK(
        private val oldList: List<ItemsItem>,
        private val newList: List<ItemsItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem].id == newList[newItem].id
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem] == newList[newItem]
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    override fun getItemCount(): Int = listUser.size

    fun ListUser(newList: List<ItemsItem>) {
        val diffResult = DiffUtil.calculateDiff(DIFFCALLBACK(listUser, newList))
        listUser.clear()
        listUser.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(picture)
                username.text = user.login
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }
}