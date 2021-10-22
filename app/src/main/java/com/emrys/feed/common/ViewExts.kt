package com.emrys.feed.common

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.gone() {
    isVisible = false
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isInvisible = true
}