package com.asrul.github.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.transform.RoundedCornersTransformation
import com.asrul.github.R
import com.asrul.github.data.User
import com.asrul.github.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val CORNER_RADIUS = 24f
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val user: User? = intent.getParcelableExtra(EXTRA_USER)

        binding.tvName.text = user?.name
        binding.tvUsername.text = getString(R.string.username, user?.username)
        binding.tvRepository.text = getString(R.string.repository, user?.repository)
        binding.tvFollower.text = user?.followers
        binding.tvFollowing.text = user?.following
        binding.tvLocation.text = user?.location
        binding.tvCompany.text = user?.company
        binding.imgAvatar.load(user?.avatar!!) {
            transformations(RoundedCornersTransformation(CORNER_RADIUS))
        }

        binding.fabBack.setOnClickListener {
            onBackPressed()
        }

        binding.fabShare.setOnClickListener {
            val text = getString(R.string.share, user.name, user.username)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val share = Intent(Intent.createChooser(intent, null))
            startActivity(share)
        }

    }
}