package com.emrys.feed.player

import android.content.Context
import android.net.Uri
import com.emrys.feed.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Util

fun Context.getExoPlayer(
    repeatMode: Int = Player.REPEAT_MODE_OFF,
    @C.AudioContentType contentType: Int = C.CONTENT_TYPE_MUSIC,
    handleAudioFocus: Boolean = true
): SimpleExoPlayer {
    val renderers = DefaultRenderersFactory(this).setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
    val player = SimpleExoPlayer.Builder(this, renderers)
        .build()
    if (handleAudioFocus) {
        player.setAudioAttributes(getAudioAttributes(contentType), true)
    }
    player.repeatMode = repeatMode
    return player
}

fun getAudioAttributes(@C.AudioContentType contentType: Int = C.CONTENT_TYPE_MUSIC): AudioAttributes {
    return AudioAttributes.Builder()
        .setContentType(contentType) // type for movie, audio and others
        .setUsage(C.USAGE_MEDIA)
        .build()
}

// build media source according to the type of uri
// all the type except HLS will use the dataSourceFactory
// for HLS types, create ignore the dataSourceFactory param and create its own cuz HLS doesn't work with all data source types
fun buildMediaSource(
    context: Context,
    uri: Uri,
    userAgent: String,
    dataSourceFactory: DataSource.Factory = context.getDefaultDataSource(userAgent)
): MediaSource {
    val mediaItem = MediaItem.fromUri(uri)
    return when (val type = Util.inferContentType(uri)) {
        C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        C.TYPE_HLS -> {
            // create own data source factory for HLS types
            HlsMediaSource.Factory(context.getDefaultDataSource(userAgent))
                .createMediaSource(mediaItem)
        }
        C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            mediaItem
        )
        else -> throw IllegalStateException("Unsupported type: $type")
    }
}

fun Context.buildMediaSource(
    uri: Uri
): MediaSource {
    return buildMediaSource(
        context = this,
        uri = uri,
        userAgent = getUserAgent()
    )
}

fun Context.getUserAgent() = Util.getUserAgent(this, getString(R.string.player_user_agent))

fun getCacheDataSourceFactory(
    context: Context,
    userAgent: String
): CacheDataSource.Factory {
    val cache = ExoplayerCacheProvider.instance.getSimpleCache(context.applicationContext)
    return CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(context.getDefaultDataSource(userAgent))
}

fun Context.getDefaultDataSource(userAgent: String): DataSource.Factory {
    return DefaultDataSourceFactory(this, userAgent)
}