package com.emrys.feed.player

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class PlaybackHelper(
    private val applicationContext: Context,
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver {

    protected val playerHelper = PlayerHelper(
        applicationContext = applicationContext,
        lifecycleOwner = lifecycleOwner
    )

    protected var previousPos = -1
}
