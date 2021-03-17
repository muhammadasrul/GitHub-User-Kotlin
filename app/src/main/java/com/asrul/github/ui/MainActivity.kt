package com.asrul.github.ui

import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.asrul.github.R
import com.asrul.github.data.User
import com.asrul.github.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var name: Array<String>
    private lateinit var username: Array<String>
    private lateinit var location: Array<String>
    private lateinit var company: Array<String>
    private lateinit var followers: Array<String>
    private lateinit var following: Array<String>
    private lateinit var repository: Array<String>
    private lateinit var avatar: TypedArray
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        setSupportActionBar(binding.toolBar)
        binding.collapsingToolbar.apply {
            title = getString(R.string.home)
            setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.mainFontColor))
            setExpandedTitleColor(ContextCompat.getColor(context, R.color.mainFontColor))
        }

        prepareUser()
        addUser()
        showUser()
    }

    private fun prepareUser() {
        name = resources.getStringArray(R.array.name)
        username = resources.getStringArray(R.array.username)
        location = resources.getStringArray(R.array.location)
        company = resources.getStringArray(R.array.company)
        followers = resources.getStringArray(R.array.followers)
        following = resources.getStringArray(R.array.following)
        repository = resources.getStringArray(R.array.repository)
        avatar = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addUser() {
        for (position in name.indices) {
            val user = User(
                name[position],
                username[position],
                location[position],
                company[position],
                repository[position],
                followers[position],
                following[position],
                avatar.getResourceId(position, -1)
            )
            users.add(user)
        }
    }

    private fun showUser() {
        val adapter = UserAdapter(users)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intent)
            }
        })
    }
}