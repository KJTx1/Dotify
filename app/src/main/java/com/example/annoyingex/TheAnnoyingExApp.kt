package com.example.annoyingex

import android.app.Application
import com.example.annoyingex.BackgroundWorkerManager

class TheAnnoyingExApp: Application() {
    lateinit var backgroundWorkerManager: BackgroundWorkerManager
        private set

    lateinit var twoDaysBackgroundWorkerManager: TwoDaysBackgroundWorkerManager
        private set

    lateinit var notificationManager: NotificationManager
        private set

    lateinit var apiManager: ApiManager

    override fun onCreate() {
        super.onCreate()

        backgroundWorkerManager = BackgroundWorkerManager(this)
        twoDaysBackgroundWorkerManager = TwoDaysBackgroundWorkerManager(this)
        notificationManager = NotificationManager(this)
        apiManager = ApiManager(this)
    }
}