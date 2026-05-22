package com.example.smarttaskmanager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.smarttaskmanager.notification.NotificationHelper
import com.example.smarttaskmanager.ui.screens.TaskApp
import com.example.smarttaskmanager.ui.theme.SmartTaskManagerTheme
import com.example.smarttaskmanager.worker.ReminderScheduler

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* permission granted or denied */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            Log.d("MainActivity", "Starting initialization...")
            NotificationHelper.createChannel(this)
            Log.d("MainActivity", "Notification channel created")

            requestPostNotificationsIfNeeded()
            Log.d("MainActivity", "Permission requested")

            ReminderScheduler.schedulePeriodicReminder(this)
            Log.d("MainActivity", "Reminder scheduled")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error during initialization: ${e.message}", e)
            e.printStackTrace()
        }

        try {
            setContent {
                SmartTaskManagerTheme {
                    TaskApp()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting content: ${e.message}", e)
            throw e
        }
    }

    private fun requestPostNotificationsIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}