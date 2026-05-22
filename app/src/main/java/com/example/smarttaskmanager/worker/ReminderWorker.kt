package com.example.smarttaskmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smarttaskmanager.data.TaskDatabase
import com.example.smarttaskmanager.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val dao = TaskDatabase.getDatabase(applicationContext).taskDao()
                val tasks = dao.getPendingTasksForReminder()
                val now = System.currentTimeMillis()
                val oneHourMillis = 60 * 60 * 1000L

                Log.d("ReminderWorker", "Checking ${tasks.size} pending tasks")

                tasks.forEach { task ->
                    val timeLeft = task.dueDateMillis - now

                    Log.d(
                        "ReminderWorker",
                        "Task: ${task.title}, Time left: ${timeLeft}ms, Due: ${task.dueDateMillis}, Now: $now"
                    )

                    // If task is due in 1 hour or less and notification not yet sent
                    if (timeLeft in 0..oneHourMillis && !task.notificationSent) {
                        Log.d("ReminderWorker", "Sending notification for: ${task.title}")
                        NotificationHelper.showReminderNotification(applicationContext, task.title, timeLeft)
                        dao.updateTask(task.copy(notificationSent = true))
                    }
                }

                Result.success()
            } catch (e: Exception) {
                Log.e("ReminderWorker", "Error in worker: ${e.message}", e)
                Result.retry()
            }
        }
    }
}