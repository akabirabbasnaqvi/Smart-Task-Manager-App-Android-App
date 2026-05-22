package com.example.smarttaskmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smarttaskmanager.data.Task
import com.example.smarttaskmanager.viewmodel.TaskViewModel

enum class TaskFilter {
    ALL, PENDING, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Int) -> Unit
) {
    val tasks = taskViewModel.tasks.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val filteredTasks = tasks.filter { task ->
        val matchesSearch =
            task.title.contains(searchQuery, ignoreCase = true) ||
                    task.description.contains(searchQuery, ignoreCase = true) ||
                    task.category.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            TaskFilter.ALL -> true
            TaskFilter.PENDING -> !task.isCompleted
            TaskFilter.COMPLETED -> task.isCompleted
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            DashboardScreen(tasks)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Smart Task Manager",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search tasks") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedFilter == TaskFilter.ALL,
                    onClick = { selectedFilter = TaskFilter.ALL },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == TaskFilter.PENDING,
                    onClick = { selectedFilter = TaskFilter.PENDING },
                    label = { Text("Pending") }
                )
                FilterChip(
                    selected = selectedFilter == TaskFilter.COMPLETED,
                    onClick = { selectedFilter = TaskFilter.COMPLETED },
                    label = { Text("Completed") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks found")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredTasks) { task ->
                        EnhancedTaskItem(
                            task = task,
                            onToggleComplete = { taskViewModel.toggleCompleted(task) },
                            onDelete = { taskViewModel.deleteTask(task) },
                            onEdit = { onEditTaskClick(task.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedTaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val isOverdue = !task.isCompleted && task.dueDateMillis < System.currentTimeMillis()

    val cardColor = if (isOverdue) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                TaskPriorityChip(priority = task.priority)

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onToggleComplete) {
                    Icon(
                        imageVector = if (task.isCompleted) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.RadioButtonUnchecked
                        },
                        contentDescription = "Toggle Complete"
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = task.description)

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Category: ${task.category}")
            Text(text = "Recurrence: ${task.recurrence}")
            Text(text = "Due: ${formatDatetime(task.dueDateMillis)}")

            if (isOverdue) {
                Text(
                    text = "OVERDUE",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            val subtasksList = parseSubtasks(task.subtasks)
            if (subtasksList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Subtasks:",
                    fontWeight = FontWeight.SemiBold
                )
                subtasksList.forEach { subtask ->
                    Text(text = "• $subtask")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }

                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun TaskPriorityChip(priority: String) {
    val color = when (priority.lowercase()) {
        "high" -> MaterialTheme.colorScheme.errorContainer
        "medium" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = priority,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

fun parseSubtasks(subtasks: String): List<String> {
    if (subtasks.isBlank()) return emptyList()
    return subtasks.split("||").map { it.trim() }.filter { it.isNotBlank() }
}