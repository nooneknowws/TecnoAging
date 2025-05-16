package com.tecno.aging

import android.app.Application
import com.tecno.aging.data.local.SessionManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TecnoAgingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}