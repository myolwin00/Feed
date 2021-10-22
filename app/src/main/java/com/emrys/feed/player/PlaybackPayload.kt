package com.emrys.feed.player

import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView

data class PlaybackPayload(
    val playerView: PlayerView,
    val url: String
)
