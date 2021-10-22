package com.emrys.feed.feature.shorts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.emrys.feed.player.PlaybackRecyclerHelper
import com.emrys.feed.R
import com.emrys.feed.common.viewBinding
import com.emrys.feed.databinding.FragmentShortsBinding

class ShortsFragment : Fragment(R.layout.fragment_shorts) {

    private val binding by viewBinding(FragmentShortsBinding::bind)
    private val viewModel by viewModels<ShortsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shortsAdapter = ShortsAdapter()
        val snapHelper = PagerSnapHelper()
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFeed.adapter = shortsAdapter
        snapHelper.attachToRecyclerView(binding.rvFeed)

        val playbackHelper = PlaybackRecyclerHelper(
            applicationContext = requireContext().applicationContext,
            lifecycleOwner = viewLifecycleOwner,
            recyclerView = binding.rvFeed
        )

        viewModel.shorts.observe(viewLifecycleOwner) {
            binding.srRefresh.isRefreshing = false
            shortsAdapter.submitList(it)
        }

        binding.srRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}