package com.asrul.github.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asrul.github.data.network.ApiConfig
import com.asrul.github.data.network.response.SearchItem
import com.asrul.github.data.network.response.SearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _searchUser = MutableLiveData<List<SearchItem?>>()
    var searchUser: LiveData<List<SearchItem?>>? = _searchUser

    fun getSearchUser(query: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getSearchUser(query)
            .enqueue(object: Callback<SearchUserResponse> {
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!.items
                        _searchUser.postValue(data)
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }
}