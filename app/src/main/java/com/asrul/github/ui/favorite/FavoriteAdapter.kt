package com.asrul.github.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.asrul.github.R
import com.asrul.github.data.db.FavUserEntity
import com.asrul.github.databinding.ListUserBinding

class FavoriteAdapter(private var favUserList: List<FavUserEntity>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        val user = favUserList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = favUserList.size

    inner class FavoriteViewHolder(private val binding: ListUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavUserEntity?) {
            with(binding) {
                tvUsername.text = user?.username
                tvType.text = user?.type

                imgAvatar.load(user?.avatarUrl) {
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
        fun onItemClicked(user: FavUserEntity?)
    }
}
