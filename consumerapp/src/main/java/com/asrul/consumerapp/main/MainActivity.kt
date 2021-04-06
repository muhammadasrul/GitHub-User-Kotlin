package com.asrul.consumerapp.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asrul.consumerapp.R
import com.asrul.consumerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainViewModel::class.java]

        initToolBar()
        observeUser()
    }

    private fun observeUser() {
        viewModel.getUserList()
        viewModel.userList.observe(this, {
            val mainAdapter = MainAdapter(it)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = mainAdapter
                setHasFixedSize(true)
            }

            binding.lottieEmpty.root.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        binding.collapsingToolbar.apply {
            title = getString(R.string.home)
            setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.white))
            setExpandedTitleColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}