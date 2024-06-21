package com.alfin.githubappuser.data.retrofit

import com.alfin.githubappuser.data.response.ItemsItem
import com.alfin.githubappuser.data.response.ResponseGitHub
import com.alfin.githubappuser.data.response.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun Users(): Call<List<ItemsItem>>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<ResponseUser>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<ResponseGitHub>

}