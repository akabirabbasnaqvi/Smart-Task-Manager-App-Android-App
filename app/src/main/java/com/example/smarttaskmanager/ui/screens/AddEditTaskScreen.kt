package com.example.smarttaskmanager.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smarttaskmanager.data.Task
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    title: String,
    initialTask: Task? = null,
    onSave: (Task) -> Unit,
    onBack: () -> Unit
) {
    var taskTitle by remember { mutableStateOf(initialTask?.title ?: "") }
    var taskDescription by remember { mutableStateOf(initialTask?.description ?: "") }
    var taskDueDateMillis by remember { mutableStateOf(initialTask?.dueDateMillis ?: System.currentTimeMillis()) }
    var taskPriority by remember { mutableStateOf(initialTask?.priority ?: "Medium") }
    var taskCategory by remember { mutableStateOf(initialTask?.category ?: "General") }
    var taskRecurrence by remember { mutableStateOf(initialTask?.recurrence ?: "None") }
    var subtaskInput by remember { mutableStateOf("") }
    var subtaskList by remember { mutableStateOf(parseSubtasks(initialTask?.subtasks ?: "")) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        timeInMillis = taskDueDateMillis
    }

    fun updateDateAndTime(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int) {
        calendar.set(year, month, dayOfMonth, hour, minute)
        taskDueDateMillis = calendar.timeInMillis
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    updateDateAndTime(year, month, dayOfMonth, hour, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Due Date & Time: ${formatDatetime(taskDueDateMillis)}")
            }

            OutlinedTextField(
                value = taskPriority,
                onValueChange = { taskPriority = it },
                label = { Text("Priority") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = taskCategory,
                onValueChange = { taskCategory = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                // simple text field replacement
            }

            OutlinedTextField(
                value = taskRecurrence,
                onValueChange = { taskRecurrence = it },
                label = { Text("Recurrence (None/Daily/Weekly/Monthly)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subtaskInput,
                onValueChange = { subtaskInput = it },
                label = { Text("Add Subtask") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (subtaskInput.isNotBlank()) {
                        subtaskList = subtaskList + subtaskInput.trim()
                        subtaskInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Subtask")
            }

            if (subtaskList.isNotEmpty()) {
                Text("Subtasks:")
                subtaskList.forEach { subtask ->
                    Text("• $subtask")
                }
            }

            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onSave(
                            Task(
                                title = taskTitle,
                                description = taskDescription,
                                dueDateMillis = taskDueDateMillis,
                                priority = taskPriority,
                                category = taskCategory,
                                recurrence = taskRecurrence,
                                subtasks = subtaskList.joinToString("||"),
                                isCompleted = initialTask?.isCompleted ?: false,
                                notificationSent = initialTask?.notificationSent ?: false
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}

fun formatDatetime(timeInMillis: Long): String {
    val formatter = java.text.SimpleDateFormat(
        "dd/MM/yyyy hh:mm a",
        Locale.getDefault()
    )
    return formatter.format(Date(timeInMillis))
}