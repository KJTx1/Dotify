package com.example.annoyingex

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val theAnnoyingExApp = (applicationContext as TheAnnoyingExApp)

        btnStart.setOnClickListener() {
            theAnnoyingExApp.backgroundWorkerManager.startWork()
            theAnnoyingExApp.twoDaysBackgroundWorkerManager.startWork()
        }

        btnBlock.setOnClickListener() {
            theAnnoyingExApp.backgroundWorkerManager.stopWork()
            theAnnoyingExApp.twoDaysBackgroundWorkerManager.stopWork()
        }

        var text = intent.getStringExtra("msgText")
        if (text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                msgBox.text = Html.fromHtml("<b>" + resources.getString(R.string.notification_message) + "</b><br/>" + text, Html.FROM_HTML_MODE_LEGACY) // for 24 api and more
            } else {
                @Suppress("DEPRECATION")
                msgBox.text = Html.fromHtml("<b>" + resources.getString(R.string.notification_message) + "</b><br/>" + text) // or for older api
            }
        }
    }
}
