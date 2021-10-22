package com.emrys.feed.player

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView

class PlayerHelper(
    private val applicationContext: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val shouldResume: () -> Boolean = { true }
): LifecycleObserver {

    private val userAgent = applicationContext.getUserAgent()

    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun addListener(listener: Player.EventListener) {
        getPlayer().addListener(listener)
    }

    fun removeListener(listener: Player.EventListener) {
        getPlayer().removeListener(listener)
    }

    fun prepare(url: String) {
        prepare(url.toUri())
    }

    fun prepare(uri: Uri) {
        val mediaSource = buildMediaSource(uri)
        getPlayer().let {
            it.setMediaSource(mediaSource)
            it.prepare()
        }
    }

    fun prepare(uris: List<String>) {
        val mediaItems = uris.map { MediaItem.fromUri(it.toUri()) }
//        val mediaSource = buildMediaSource(uris.first()!!.toUri())
        getPlayer().let {
//            it.setMediaSource(mediaSource)
            it.clearMediaItems()
            it.addMediaItems(mediaItems)
            it.prepare()
        }
    }

    fun attachPlayerView(playerView: PlayerView) {
        getPlayer().let {
            playerView.player = it
            this.playerView = playerView
        }
    }

    fun switchPlayerView(newPlayerView: PlayerView) {
        if (playerView == null) {
            attachPlayerView(newPlayerView)
            return
        }

        getPlayer().let {
            PlayerView.switchTargetView(it, playerView, newPlayerView)
            this.playerView = newPlayerView
        }
    }

    fun detachPlayerView() {
        playerView?.player = player
    }

    fun isReady() = getPlayer().playbackState == Player.STATE_READY

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun play() {
        if (shouldResume()) {
            getPlayer().play()
        }
    }

    fun play(pos: Int) {
        getPlayer().seekTo(pos, 0L)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        getPlayer().pause()
    }

    fun stop() {
        getPlayer().stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() {
        player?.let {
            it.stop()
            it.release()
            player = null
        }
    }

    private fun getPlayer(): ExoPlayer {
        return player ?: kotlin.run {
            applicationContext.getExoPlayer()
                .also {
                    player = it
                }
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        // build cache factory
        return getCacheDataSourceFactory(applicationContext, userAgent)
            // build media source according to the type of uri
            .let { cacheDataSourceFactory ->
                buildMediaSource(applicationContext, uri, userAgent, cacheDataSourceFactory)
            }.let {
                LoopingMediaSource(it)
            }
    }
}
