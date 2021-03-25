package com.asrul.github.ui.detail.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asrul.github.data.network.ApiConfig
import com.asrul.github.data.network.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    private var _following = MutableLiveData<List<UserResponse>>()
    val following: LiveData<List<UserResponse>> = _following

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowing(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getFollowing(username)
            .enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()
                        _following.postValue(data)
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }
}