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
import kotlinx.coroutines.runBlocking

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favUserDao: FavUserDAO = AppDatabase.getDatabase(application).favUserDAO()

    lateinit var userFromDB: LiveData<List<FavUserEntity>>

    fun getUserFromDB() {
        runBlocking(Dispatchers.IO) {
            userFromDB = favUserDao.getAllUser()
        }
    }

}