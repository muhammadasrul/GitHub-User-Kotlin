package com.asrul.github.data.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavUserDAO {
    @Query("SELECT * FROM favorite_user")
    fun getAllUser(): List<FavUserEntity>

    @Query("SELECT * FROM favorite_user WHERE username = :username")
    fun getByUsername(username: String): FavUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserToFav(user: FavUserEntity)

    @Delete
    fun deleteUserFromFav(user: FavUserEntity)

    //ContentProvider
    @Query("SELECT * FROM favorite_user")
    fun cursorGetAllUser(): Cursor
}