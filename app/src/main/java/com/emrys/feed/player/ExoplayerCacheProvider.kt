package com.emrys.feed.player

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class ExoplayerCacheProvider private constructor() {

    companion object {

        const val CACHE_FILE_NAME = "media"
        private const val CACHE_FILE_SIZE_IN_MB : Long = 500

        val instance by lazy { ExoplayerCacheProvider() }
    }

    private var cache: SimpleCache? = null

    // context must be application context
    fun getSimpleCache(context: Context): SimpleCache {
        return cache ?: kotlin.run {
            val evictor = LeastRecentlyUsedCacheEvictor(CACHE_FILE_SIZE_IN_MB * 1024 * 1024)
            val file = File(context.cacheDir, CACHE_FILE_NAME)
            SimpleCache(file, evictor, ExoDatabaseProvider(context)).also { cache = it }
        }
    }


}