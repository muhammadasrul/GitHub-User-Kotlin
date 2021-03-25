package com.asrul.github.ui.detail.following

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asrul.github.data.network.response.UserResponse
import com.asrul.github.databinding.FragmentFollowingBinding
import com.asrul.github.ui.detail.UserAdapter
import com.asrul.github.ui.detail.DetailActivity
import com.asrul.github.util.setGone
import com.asrul.github.util.setVisible

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFollowing()
    }

    private fun observeFollowing() {
        val activity = activity as DetailActivity
        val username = activity.getUsername()
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
        viewModel.getFollowing(username)
        viewModel.following.observe(viewLifecycleOwner, {
            val adapter = UserAdapter(it)
            binding.apply {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter

                if (it.isEmpty()) lottieEmpty.root.setVisible() else lottieEmpty.root.setGone()
            }
            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: UserResponse?) {
                    val intent = Intent(context, DetailActivity::class.java)
                        .putExtra(DetailActivity.USERNAME, user?.login)
                    startActivity(intent)
                }
            })
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) binding.progressBar.setVisible() else binding.progressBar.setGone()
        })
    }
}