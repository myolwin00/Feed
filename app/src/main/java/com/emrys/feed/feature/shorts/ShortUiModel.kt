package com.emrys.feed.feature.shorts

import androidx.recyclerview.widget.DiffUtil
import com.emrys.feed.data.FeedText
import com.emrys.feed.data.FeedVideo

sealed class ShortUiModel(
    val id: String
) {
    data class Video(val data: FeedVideo): ShortUiModel(id = data.id)
    data class Text(val data: FeedText): ShortUiModel(id = data.id)

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<ShortUiModel>() {
            override fun areItemsTheSame(oldItem: ShortUiModel, newItem: ShortUiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShortUiModel, newItem: ShortUiModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}

