package com.emrys.feed.player

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player

class PlaybackRecyclerHelper(
    private val applicationContext: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val recyclerView: RecyclerView,
) : PlaybackHelper(applicationContext, lifecycleOwner) {

    private val playbackStateListener = object : Player.EventListener {}

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        var byUser = false

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val pos = getCurrentVisiblePosition()
                    onScrollChanged(pos, byUser)
                    byUser = false
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    byUser = true
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val pos = getCurrentVisiblePosition()
            onScrollChanged(pos, byUser)
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerCallback() {
        recyclerView.addOnScrollListener(scrollListener)
        playerHelper.addListener(playbackStateListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterCallback() {
        recyclerView.removeOnScrollListener(scrollListener)
        playerHelper.removeListener(playbackStateListener)
    }

    fun onScrollChanged(position: Int, byUser: Boolean) {
        if (position != -1 && previousPos != position) {

            // stop previously playing one
            playerHelper.stop()

            val playbackHolder = getPlaybackHolder(position)

            playbackHolder?.let {
                val payload = playbackHolder.getPlayload()
                playerHelper.prepare(payload.url)
                playerHelper.play()
                playerHelper.switchPlayerView(payload.playerView)
            } ?: kotlin.run {
                // do nothing for non-playback holders
            }

            previousPos = position
        } else {
            // do nothing when the pos is the same with previous pos
        }
    }

    private fun getCurrentVisiblePosition() = (recyclerView.layoutManager as LinearLayoutManager)
        .findFirstVisibleItemPosition()

    private fun getPlaybackHolder(pos: Int) = recyclerView.findViewHolderForAdapterPosition(pos)
        ?.let { it as? PlaybackHolder }

}
