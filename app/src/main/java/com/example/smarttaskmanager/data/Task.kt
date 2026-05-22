package com.example.smarttaskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDateMillis: Long,
    val priority: String,
    val category: String = "General",
    val recurrence: String = "None",
    val subtasks: String = "",
    val isCompleted: Boolean = false,
    val notificationSent: Boolean = false
)