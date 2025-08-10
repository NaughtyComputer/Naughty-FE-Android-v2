package com.daemon.tuzamate_v2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TuzamateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}