package com.asrul.github.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asrul.github.data.db.AppDatabase
import com.asrul.github.data.db.FavUserDAO
import com.asrul.github.data.db.FavUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favUserDao: FavUserDAO = AppDatabase.getDatabase(application).favUserDAO()

    private var _userFromDB = MutableLiveData<List<FavUserEntity>>()
    val userFromDB: LiveData<List<FavUserEntity>> = _userFromDB

    fun getUserFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = favUserDao.getAllUser()
            _userFromDB.postValue(data)
        }
    }

}