package com.example.annoyingex

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class sendMsgWorker(private val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    private var msgList: List<String>? = null

    override fun doWork(): Result {
        val theAnnoyingExApp = (applicationContext as TheAnnoyingExApp)

        if (msgList == null) {
            theAnnoyingExApp.apiManager.getMsgs ({ allMsgs ->
                msgList = allMsgs

                var index = Random.nextInt(0, msgList!!.size)
                theAnnoyingExApp.notificationManager.pushNotification(msgList!![index])
            },
                {
                    theAnnoyingExApp.notificationManager.pushNotification("unable to retrieve message")
                })
        }

        return Result.success()
    }
}