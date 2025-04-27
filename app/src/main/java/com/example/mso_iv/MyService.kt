package com.example.mso_iv

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import androidx.lifecycle.LifecycleService
import kotlin.properties.Delegates


class MyService : LifecycleService() {
    private val binder = LocalBinder()
    private var startTime by Delegates.notNull<Long>()

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }
    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        startTime = SystemClock.elapsedRealtime()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getRunningTime(): Int {
        return ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()
    }



}

