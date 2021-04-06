package com.asrul.consumerapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.asrul.consumerapp.R
import com.asrul.consumerapp.data.User
import com.asrul.consumerapp.databinding.ListUserBinding

class MainAdapter(private val userList: List<User>): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = userList.size

    inner class MainViewHolder(private val binding: ListUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUsername.text = user.username
                tvType.text = user.type
                imgAvatar.load(user.avatarUrl) {
                    placeholder(R.drawable.ic_profile)
                    transformations(CircleCropTransformation())
                }

                itemView.setOnClickListener {
                    Toast.makeText(root.context, root.resources.getString(R.string.consumer_toast), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}