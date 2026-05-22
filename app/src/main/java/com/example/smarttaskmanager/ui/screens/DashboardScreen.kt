package com.example.smarttaskmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smarttaskmanager.data.Task

@Composable
fun DashboardScreen(tasks: List<Task>) {
    val total = tasks.size
    val completed = tasks.count { it.isCompleted }
    val pending = total - completed
    val overdue = tasks.count { !it.isCompleted && it.dueDateMillis < System.currentTimeMillis() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("Total: $total") })
                AssistChip(onClick = {}, label = { Text("Done: $completed") })
                AssistChip(onClick = {}, label = { Text("Pending: $pending") })
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("Overdue: $overdue") })
                AssistChip(onClick = {}, label = { Text("Progress: ${if (total == 0) 0 else (completed * 100 / total)}%") })
            }
        }
    }
}