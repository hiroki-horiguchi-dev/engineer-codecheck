/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchBinding
import jp.co.yumemi.android.code_check.network.model.FilteredItem
import jp.co.yumemi.android.code_check.ui.search.adapter.GithubRepositoryAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchFragmentViewModel>()

    private lateinit var adapter: GithubRepositoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        adapter = GithubRepositoryAdapter(object : GithubRepositoryAdapter.OnItemClickListener {
            override fun itemClick(item: FilteredItem) {
                gotoRepositoryFragment(item)
            }

            override fun favoriteClick(item: FilteredItem, position: Int) {
                if (item.isFavorite) {
                    deleteFavorite(item, position)
                } else {
                    registerFavorite(item, position)
                }
            }
        })

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        search(adapter)
        watchErrorMessage()
        watchAddFavoritePosition()
        watchRemoveFavoritePosition()
    }

    private fun deleteFavorite(item: FilteredItem, position: Int) {
        viewModel.deleteFavorite(item, position)
    }

    private fun registerFavorite(item: FilteredItem, position: Int) {
        viewModel.registerFavorite(item, position)
    }

    /**
     * お気に入り登録された時の UI 反映
     */
    private fun watchAddFavoritePosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isUnRemoveFavoritePosition.collect {
                    if (it >= 0) {
                        adapter.notifyItemChanged(it)
                    }
                }
            }
        }
    }

    /**
     * お気に入り削除された時の UI 反映
     */
    private fun watchRemoveFavoritePosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAddFavoritePosition.collect {
                    if (it >= 0) {
                        adapter.notifyItemChanged(it)
                    }
                }
            }
        }
    }

    /**
     * テキスト入力と検索ボタン押下時の処理
     */
    private fun search(adapter: GithubRepositoryAdapter) {
        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                val searchText = editText.text.toString()
                if (action == EditorInfo.IME_ACTION_SEARCH && searchText.isNotEmpty()) {
                    fetchSearchResult(
                        adapter,
                        searchText
                    )
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
    }

    /**
     * Github リポジトリの検索
     */
    private fun fetchSearchResult(
        adapter: GithubRepositoryAdapter,
        searchText: String
    ) {
        viewModel.search(searchText)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    adapter.submitList(it)
                    return@collect
                }
            }
        }
    }

    /**
     * TwoFragment へ遷移
     */
    private fun gotoRepositoryFragment(item: FilteredItem) {
        val directions =
            SearchFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(directions)
    }

    /**
     * API エラーメッセージの監視
     */
    private fun watchErrorMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect {
                    if (it.isNotEmpty()) showToast(it)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
