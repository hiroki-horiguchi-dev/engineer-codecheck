/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        val item = args.item
        val context = requireContext()

        binding.ownerIconView.load(item.owner?.avatarUrl)
        binding.nameView.text = item.name
        binding.languageView.text = item.language
        binding.starsView.text = context.getString(R.string.stars_count, item.stargazersCount)
        binding.watchersView.text =
            context.getString(R.string.watchers, item.watchersCount)
        binding.forksView.text = context.getString(R.string.forks, item.forksCount)
        binding.openIssuesView.text = context.getString(R.string.open_issues, item.openIssuesCount)
        if (item.isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.baseline_unfavorite_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
