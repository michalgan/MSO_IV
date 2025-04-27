package com.example.mso_iv

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    private lateinit var mService: MyService
    private var mBound: Boolean = false
    private lateinit var timer: Timer
    private lateinit var timeTextView: TextView

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
            mBound = true
            startTimer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
            stopTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timeTextView = findViewById(R.id.timeTextView)


        val intent = Intent(this, MyService::class.java)
        startService(intent)

        val stopTimer = findViewById<Button>(R.id.stopButton)
        stopTimer.setOnClickListener{
            stopTimer()
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(serviceConnection)
            mBound = false
        }
        stopTimer()
    }

    private fun startTimer() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                runOnUiThread {
                    if (mBound) {
                        val seconds = mService.getRunningTime()
                        timeTextView.text = "Service running for: $seconds seconds"
                    }
                }
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }


}
