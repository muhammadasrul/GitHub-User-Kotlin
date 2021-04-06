package com.asrul.consumerapp.main

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asrul.consumerapp.data.User

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val contentResolver = application.contentResolver

    companion object {
        private const val TABLE_NAME = "favorite_user"
        private const val AUTHORITY = "com.asrul.github"

        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val AVATAR_URL = "avatar_url"
        private const val FOLLOWERS = "followers"
        private const val FOLLOWING = "following"
        private const val PUBLIC_REPOS = "public_repos"
        private const val LOCATION = "location"
        private const val COMPANY = "company"
        private const val TYPE = "type"

        val uri: Uri = Uri.Builder()
            .scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    fun getUserList() {
        _userList.postValue(getUser())
    }

    private fun getUser(): List<User> {
        val list: MutableList<User> = mutableListOf()

        contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.apply {
            while (moveToNext()) {
                list.add(
                    User(
                        getString(getColumnIndexOrThrow(USERNAME)),
                        getString(getColumnIndexOrThrow(NAME)),
                        getString(getColumnIndexOrThrow(AVATAR_URL)),
                        getString(getColumnIndexOrThrow(FOLLOWERS)),
                        getString(getColumnIndexOrThrow(FOLLOWING)),
                        getString(getColumnIndexOrThrow(PUBLIC_REPOS)),
                        getString(getColumnIndexOrThrow(LOCATION)),
                        getString(getColumnIndexOrThrow(COMPANY)),
                        getString(getColumnIndexOrThrow(TYPE))
                    )
                )
            }
            close()
        }
        return list.toList()
    }
}