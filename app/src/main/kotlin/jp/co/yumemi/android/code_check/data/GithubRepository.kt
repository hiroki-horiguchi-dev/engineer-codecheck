package jp.co.yumemi.android.code_check.data

import GithubResponse
import jp.co.yumemi.android.code_check.network.service.GithubClient
import retrofit2.Response
import javax.inject.Inject

interface GithubRepository {
    suspend fun fetchGithubRepository(
        searchText: String
    ): Response<GithubResponse>
}

class GithubRepositoryImpl @Inject constructor(
    private val githubClient: GithubClient
) : GithubRepository {

    override suspend fun fetchGithubRepository(searchText: String): Response<GithubResponse> =
        githubClient.fetchGithubRepositoryList(
            searchText = searchText
        )
}