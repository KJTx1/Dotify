package com.example.annoyingex

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class TwoDaysBackgroundWorkerManager(private val context: Context) {
    private var workManager = WorkManager.getInstance(context)

    companion object {
        const val WORK_REQUEST_TAG = "WORK_REQUEST_TAG"
    }

    fun startWork() {

        if(isRunning()) {
            stopWork()
        }

        val workConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<sendMsgWorker>(2, TimeUnit.DAYS)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setConstraints(workConstraint)
            .addTag(WORK_REQUEST_TAG)
            .build()

        workManager.enqueue(workRequest)
    }

    private fun isRunning() : Boolean {
        return when (workManager.getWorkInfosByTag(WORK_REQUEST_TAG).get().firstOrNull()?.state) {
            WorkInfo.State.ENQUEUED,
            WorkInfo.State.RUNNING -> true
            else -> false
        }
    }

    fun stopWork() {
        workManager.cancelAllWorkByTag(WORK_REQUEST_TAG)
    }
}