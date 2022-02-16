package com.raywenderlich.android.githubrepolist.api

import com.raywenderlich.android.githubrepolist.data.RepoResult
import retrofit2.Call
import retrofit2.http.GET

interface GithubService {
    @GET("/repositories")
    suspend fun retrieveRepositories(): RepoResult

    //sample search
    @GET("/search/repositories?q=language=kotlin&sort=stars&order=desc&per_page=50")
    suspend fun searchRepositories(): RepoResult
}