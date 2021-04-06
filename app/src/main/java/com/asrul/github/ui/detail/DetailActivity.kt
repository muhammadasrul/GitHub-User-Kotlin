package com.asrul.github.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.asrul.github.R
import com.asrul.github.data.db.FavUserEntity
import com.asrul.github.databinding.ActivityDetailBinding
import com.asrul.github.ui.ViewPagerAdapter
import com.asrul.github.util.setGone
import com.asrul.github.util.setVisible

class DetailActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val CORNER_RADIUS = 24f
    }

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private lateinit var username: String

    private var favUser: FavUserEntity? = null
    private var isFavorite: Boolean = false

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[DetailViewModel::class.java]
        username = intent.getStringExtra(USERNAME).toString()

        val pagerAdapter = ViewPagerAdapter(this, supportFragmentManager)

        binding.apply {
            collapsingToolbar.apply {
                setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.mainFontColor))
                setExpandedTitleColor(ContextCompat.getColor(context, R.color.mainFontColor))
            }
            viewPager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(binding.viewPager)
            fabFavorite.setOnClickListener { addOrDeleteFavorite() }
            fabShare.setOnClickListener { shareAction() }
        }

        isFavorite()
        observeUserDetail()
    }

    private fun observeUserDetail() {
        viewModel.isLoading.observe(this, {
            if (it) binding.progressBar.setVisible() else binding.progressBar.setGone()
        })

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

            favUser = FavUserEntity(
                username = it.login.toString(),
                name = it.name,
                avatarUrl = it.avatarUrl,
                followers = it.followers.toString(),
                following = it.following.toString(),
                publicRepos = it.publicRepos.toString(),
                location = it.location,
                company = it.company.toString(),
                type = it.type
            )
        })
    }

    fun getUsername(): String {
        return username
    }

    private fun isFavorite() {
        viewModel.isFavorite.observe(this, {
            isFavorite = it
            if (it) binding.fabFavorite.setImageResource(R.drawable.ic_favorite) else binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        })
    }

    private fun addOrDeleteFavorite() {
        if (isFavorite) {
            Toast.makeText(this, getString(R.string.delete_from_fav, favUser?.username) , Toast.LENGTH_SHORT).show()
            favUser?.let { viewModel.deleteFromFavorite(it) }
        } else {
            favUser?.let { viewModel.addToFavorite(it) }
            Toast.makeText(this, getString(R.string.add_to_fav, favUser?.username), Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareAction() {
        val text = getString(R.string.share_msg, username, username)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val share = Intent.createChooser(intent, null)
        startActivity(share)
    }
}