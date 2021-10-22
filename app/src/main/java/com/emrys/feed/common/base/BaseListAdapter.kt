package com.emrys.feed.common.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T, VH: BaseViewHolder<T>>(
    val diffCallback: DiffUtil.ItemCallback<T>
): ListAdapter<T, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}