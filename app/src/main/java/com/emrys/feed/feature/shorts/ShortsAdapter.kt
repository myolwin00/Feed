package com.emrys.feed.feature.shorts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emrys.feed.player.PlaybackHolder
import com.emrys.feed.player.PlaybackPayload
import com.emrys.feed.common.base.BaseListAdapter
import com.emrys.feed.common.base.BaseViewHolder
import com.emrys.feed.databinding.ItemFeedVideoBinding
import com.emrys.feed.databinding.ItemTextBinding

class ShortsAdapter: BaseListAdapter<ShortUiModel, ShortsAdapter.ShortsHolder>(ShortUiModel.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsHolder {
        return when(viewType) {
            VIEW_TYPE_VIDEO -> {
                VideoHolder(ItemFeedVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            VIEW_TYPE_TEXT -> {
                TextHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw UnsupportedOperationException("unknown view type: ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is ShortUiModel.Video -> VIEW_TYPE_VIDEO
            is ShortUiModel.Text -> VIEW_TYPE_TEXT
        }
    }

    companion object {
        private const val VIEW_TYPE_VIDEO = 1
        private const val VIEW_TYPE_TEXT = 2
    }

    abstract class ShortsHolder(
        itemView: View
    ): BaseViewHolder<ShortUiModel>(itemView)

    inner class TextHolder(
        val binding: ItemTextBinding
    ): ShortsHolder(binding.root) {

        override fun bind(data: ShortUiModel) {
            super.bind(data)
            val uiModel = data as ShortUiModel.Text
            binding.tvText.text = uiModel.data.text
        }
    }

    inner class VideoHolder(
        val binding: ItemFeedVideoBinding
    ): ShortsHolder(binding.root), PlaybackHolder {

        override fun bind(data: ShortUiModel) {
            super.bind(data)
            val video = data as ShortUiModel.Video
        }

        override fun getPlayload(): PlaybackPayload {
            return PlaybackPayload(
                playerView = binding.playerView,
                url = (data as ShortUiModel.Video).data.url
            )
        }

    }
}