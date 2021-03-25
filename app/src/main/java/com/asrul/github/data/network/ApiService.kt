package com.asrul.github.data.network

import com.asrul.github.BuildConfig
import com.asrul.github.data.network.response.DetailUserResponse
import com.asrul.github.data.network.response.SearchUserResponse
import com.asrul.github.data.network.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users?")
    @Headers("Authorization: Token ${BuildConfig.GITHUB_KEY}")
    fun getSearchUser(
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: Token ${BuildConfig.GITHUB_KEY}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: Token ${BuildConfig.GITHUB_KEY}")
    fun getFollower(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: Token ${BuildConfig.GITHUB_KEY}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserResponse>>
}