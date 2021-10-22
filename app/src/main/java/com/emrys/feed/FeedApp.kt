package com.emrys.feed

import android.app.Application
import timber.log.Timber

class FeedApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}