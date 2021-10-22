package com.emrys.feed.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<Data>(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private var _data: Data? = null
    val data: Data
        get() = _data!!

    open fun bind(data: Data) {
        _data = data
    }
}