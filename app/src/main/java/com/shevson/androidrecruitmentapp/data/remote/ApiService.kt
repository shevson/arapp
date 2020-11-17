package com.shevson.androidrecruitmentapp.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    companion object {
        const val USERS_ENDPOINT = "users"
    }

    @GET(USERS_ENDPOINT)
    fun getUsers(): Single<List<UsersItem>>

    @GET("$USERS_ENDPOINT/{user_login}/repos")
    suspend fun getUsersRepos(
        @Path("user_login") user_login: String
    ): List<ReposItem>
}