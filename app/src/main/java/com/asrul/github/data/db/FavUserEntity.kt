package com.asrul.github.data.db

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_user")
data class FavUserEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "followers") val followers: String?,
    @ColumnInfo(name = "following") val following: String?,
    @ColumnInfo(name = "public_repos") val publicRepos: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "company") val company: String?,
    @ColumnInfo(name = "type") val type: String?
): Parcelable
