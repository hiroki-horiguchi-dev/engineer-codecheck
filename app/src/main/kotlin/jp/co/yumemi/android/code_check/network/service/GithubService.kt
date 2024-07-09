package jp.co.yumemi.android.code_check.network.service

import GithubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubService {
    @GET("search/repositories")
    suspend fun fetchGithubRepositoryList(
        @Header("Accept") accept: String,
        @Query("q") searchText: String
    ): Response<GithubResponse>
}