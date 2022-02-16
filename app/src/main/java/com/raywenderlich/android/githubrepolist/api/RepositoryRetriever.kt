package com.raywenderlich.android.githubrepolist.api

import com.raywenderlich.android.githubrepolist.data.RepoResult
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryRetriever {
    private val service: GithubService

    companion object{
        const val BASE_URL  = "http://api.github.com"
    }

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(GithubService::class.java)
    }
    suspend fun getRepositories(): RepoResult {
        return service.searchRepositories()
    }
}