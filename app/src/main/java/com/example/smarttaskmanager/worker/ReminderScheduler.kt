package com.example.smarttaskmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    fun schedulePeriodicReminder(context: Context) {
        try {
            val request = PeriodicWorkRequestBuilder<ReminderWorker>(
                5,  // Check every 5 minutes (for testing)
                TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "task_reminder_work",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )

            Log.d("ReminderScheduler", "Periodic reminder scheduled")
        } catch (e: Exception) {
            Log.e("ReminderScheduler", "Error scheduling reminder: ${e.message}", e)
        }
    }
}