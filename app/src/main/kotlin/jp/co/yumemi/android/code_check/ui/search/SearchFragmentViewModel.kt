/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import GithubResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.data.FavoriteRepository
import jp.co.yumemi.android.code_check.data.GithubRepository
import jp.co.yumemi.android.code_check.database.entity.Favorite
import jp.co.yumemi.android.code_check.network.model.FilteredItem
import jp.co.yumemi.android.code_check.network.model.Item
import jp.co.yumemi.android.code_check.network.model.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val repository: GithubRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<FilteredItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    /// TODO("RecyclerView を上手に更新する方法忘れたため、かなり雑な更新方法。改善する必要あり")
    private val _isAddFavoritePosition = MutableStateFlow(-1)
    val isAddFavoritePosition = _isAddFavoritePosition.asStateFlow()

    /// TODO("RecyclerView を上手に更新する方法忘れたため、かなり雑な更新方法。改善する必要あり")
    private val _isRemoveFavoritePosition = MutableStateFlow(-1)
    val isUnRemoveFavoritePosition = _isRemoveFavoritePosition.asStateFlow()

    fun search(inputText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetchGithubRepository(inputText)
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    _items.value = filter(body.items)
                } else {
                    _errorMessage.value = "検索結果を取得できませんでした"
                }
            } else {
                _errorMessage.value = handleErrorResponse(result)
            }
        }
    }

    private fun filter(items: List<Item>): List<FilteredItem> {
        val favoriteList = mutableListOf<FilteredItem>()
        val favorite = fetchFavoriteList()
        for (item in items) {
            val filteredItem = FilteredItem(
                name = item.name ?: "",
                owner = item.owner ?: Owner(avatarUrl = ""),
                language = item.language ?: "",
                stargazersCount = item.stargazersCount ?: 0,
                watchersCount = item.watchersCount ?: 0,
                forksCount = item.forksCount ?: 0,
                openIssuesCount = item.openIssuesCount ?: 0,
                isFavorite = favorite.contains((item.name ?: ""))
            )
            favoriteList.add(filteredItem)
        }
        return favoriteList
    }

    private fun fetchFavoriteList() = favoriteRepository.getAllFavorites().map { it.repositoryName }

    fun registerFavorite(item: FilteredItem, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (item.name.isNotBlank()) {
                favoriteRepository.insertFavorites(Favorite(repositoryName = item.name))
                _items.value.get(position).isFavorite = true
                _isAddFavoritePosition.value = position
            }
        }

    fun deleteFavorite(item: FilteredItem, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                favoriteRepository.deleteFavorites(Favorite(repositoryName = item.name))
                _items.value.get(position).isFavorite = false
                _isRemoveFavoritePosition.value = position
            }
        }

    private fun handleErrorResponse(response: Response<GithubResponse>) =
        when (response.code()) {
            403 -> "閲覧禁止です"
            404 -> "検索結果がありません"
            500 -> "サーバーエラーです"
            503 -> "メンテナンス中です"
            else -> "不明なエラーが発生しました"
        }
}