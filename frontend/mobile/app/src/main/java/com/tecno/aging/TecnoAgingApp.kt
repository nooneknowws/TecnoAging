package com.tecno.aging

import android.app.Application
import com.tecno.aging.data.local.SessionManager

class TecnoAgingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}