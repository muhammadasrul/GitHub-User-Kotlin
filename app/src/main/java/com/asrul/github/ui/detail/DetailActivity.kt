package com.asrul.github.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import com.asrul.github.R
import com.asrul.github.databinding.ActivityDetailBinding
import com.asrul.github.ui.ViewPagerAdapter
import com.asrul.github.util.setGone
import com.asrul.github.util.setVisible

class DetailActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val CORNER_RADIUS = 24f
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbar.apply {
            setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.mainFontColor))
            setExpandedTitleColor(ContextCompat.getColor(context, R.color.mainFontColor))
        }

        username = intent.getStringExtra(USERNAME)!!

        val pagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        observeUserDetail()

    }

    private fun observeUserDetail() {
        val viewModel = DetailViewModel()
        viewModel.getUserDetail(username)
        viewModel.userDetail.observe(this, {
            binding.apply {
                imgAvatar.load(it.avatarUrl) {
                    placeholder(R.drawable.ic_profile)
                    transformations(RoundedCornersTransformation(CORNER_RADIUS))
                }
                tvFollower.text = it.followers.toString()
                tvFollowing.text = it.following.toString()
                tvRepository.text = it.publicRepos.toString()
                tvLocation.text = it.location ?: getString(R.string.no_location)
                tvCompany.text = if (it.company == null) getString(R.string.no_company) else it.company.toString()
                collapsingToolbar.title = it.name ?: it.login
            }
        })
        viewModel.isLoading.observe(this, {
            if (it) binding.progressBar.setVisible() else binding.progressBar.setGone()
        })
    }

    fun getUsername(): String {
        return username
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share -> {
                val text = getString(R.string.share, username, username)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }
                val share = Intent(Intent.createChooser(intent, null))
                startActivity(share)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}