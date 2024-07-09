package jp.co.yumemi.android.code_check.network.service

import GithubResponse
import retrofit2.Response
import javax.inject.Inject

class GithubClient @Inject constructor(
    private val githubService: GithubService
) {

    suspend fun fetchGithubRepositoryList(searchText: String): Response<GithubResponse> =
        githubService.fetchGithubRepositoryList(
            accept = ACCEPT,
            searchText = searchText
        )

    companion object {
        private const val ACCEPT = "application/vnd.github.v3+json"
    }
}