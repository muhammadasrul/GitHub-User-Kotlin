package com.asrul.github.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.asrul.github.R
import com.asrul.github.data.network.response.SearchItem
import com.asrul.github.databinding.ListUserBinding

class SearchAdapter(private val searchItem: List<SearchItem?>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = searchItem[position]!!
        holder.bind(user)

    }

    override fun getItemCount(): Int = searchItem.size

    inner class SearchViewHolder(private val binding: ListUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: SearchItem) {
            with(binding) {
                tvUsername.text = user.login
                tvType.text = user.type
                imgAvatar.load(user.avatarUrl) {
                    placeholder(R.drawable.ic_profile)
                    transformations(CircleCropTransformation())
                }
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(user)
                }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(user: SearchItem)
    }
}
