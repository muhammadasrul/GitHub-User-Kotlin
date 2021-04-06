package com.asrul.github.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.asrul.github.data.db.AppDatabase
import com.asrul.github.data.db.FavUserDAO
import com.asrul.github.data.db.FavUserEntity
import com.asrul.github.data.network.ApiConfig
import com.asrul.github.data.network.response.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val favUserDao: FavUserDAO = AppDatabase.getDatabase(application).favUserDAO()
    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetail(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val dataFav = favUserDao.getByUsername(username)

            if (dataFav != null) {
                _isFavorite.postValue(true)
            } else {
                _isFavorite.postValue(false)
            }

            ApiConfig.getApiService().getUserDetail(username)
                .enqueue(object : Callback<DetailUserResponse> {
                    override fun onResponse(
                        call: Call<DetailUserResponse>,
                        response: Response<DetailUserResponse>
                    ) {
                        _isLoading.postValue(false)
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()
                            _userDetail.postValue(data)
                        }
                    }

                    override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
        }
    }

    fun addToFavorite(user: FavUserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favUserDao.addUserToFav(user)
            _isFavorite.postValue(true)
        }
    }

    fun deleteFromFavorite(user: FavUserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favUserDao.deleteUserFromFav(user)
            _isFavorite.postValue(false)
        }
    }
}