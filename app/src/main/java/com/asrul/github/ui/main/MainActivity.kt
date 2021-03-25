package com.asrul.github.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asrul.github.R
import com.asrul.github.data.network.response.SearchItem
import com.asrul.github.databinding.ActivityMainBinding
import com.asrul.github.ui.detail.DetailActivity
import com.asrul.github.util.setGone
import com.asrul.github.util.setVisible

class MainActivity : AppCompatActivity() {

    companion object {
        const val QUERY = "query"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        val query = savedInstanceState?.getString(QUERY)
        if (query != null) {
            observeSearchUser(query)
        }

        initToolBar()
        searchUser()
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        binding.collapsingToolbar.apply {
            title = getString(R.string.home)
            setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.mainFontColor))
            setExpandedTitleColor(ContextCompat.getColor(context, R.color.mainFontColor))
        }
    }

    private fun searchUser() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(q: String?): Boolean {

                    if (q != null) {
                        searchView.clearFocus()
                        observeSearchUser(q)
                    } else {
                        searchView.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(q: String?): Boolean = false
            })
        }
    }

    private fun observeSearchUser(q: String) {
        val searchUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        searchUserViewModel.getSearchUser(q)
        searchUserViewModel.searchUser?.observe(this, {
            val adapter = SearchAdapter(it)
            binding.apply {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.setHasFixedSize(true)

                if (it.isEmpty()) lottieEmpty.root.setVisible() else lottieEmpty.root.setGone()
            }
            adapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback {
                override fun onItemClicked(user: SearchItem) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                        .putExtra(DetailActivity.USERNAME, user.login)
                    startActivity(intent)
                }

            })
        })
        searchUserViewModel.isLoading.observe(this, {
            if (it) binding.progressBar.setVisible() else binding.progressBar.setGone()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (binding.searchView.query.isNotEmpty()){
            outState.putString(QUERY, binding.searchView.query.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }
}