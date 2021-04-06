package com.asrul.consumerapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String?,
    val avatarUrl: String?,
    val followers: String?,
    val following: String?,
    val publicRepos: String?,
    val location: String?,
    val company: String?,
    val type: String?
): Parcelable
