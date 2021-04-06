package com.asrul.github.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asrul.github.R
import com.asrul.github.data.db.FavUserEntity
import com.asrul.github.databinding.ActivityFavoriteBinding
import com.asrul.github.ui.detail.DetailActivity
import com.asrul.github.util.setGone
import com.asrul.github.util.setVisible

class FavoriteActivity : AppCompatActivity() {

    private val binding: ActivityFavoriteBinding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[FavoriteViewModel::class.java]
        observeFavorite()
    }

    private fun observeFavorite() {
        viewModel.getUserFromDB()
        viewModel.userFromDB.observe(this, {
            val adapter = FavoriteAdapter(it)

            binding.apply {
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter

                if (it.isEmpty()) lottieEmpty.root.setVisible() else lottieEmpty.root.setGone()
            }
            adapter.setOnClickCallback(object: FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(user: FavUserEntity?) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                        .putExtra(DetailActivity.USERNAME, user?.username)
                    startActivity(intent)
                }

            })
        })
    }
}