package com.example.recycleview

import android.app.Application

class DotifyApp: Application() {
    var listenCount: Int = 0

    lateinit var songManager: SongManager

    lateinit var apiManager: ApiManager

    override fun onCreate() {
        super.onCreate()

        apiManager = ApiManager(this)

        songManager = SongManager()

    }
}