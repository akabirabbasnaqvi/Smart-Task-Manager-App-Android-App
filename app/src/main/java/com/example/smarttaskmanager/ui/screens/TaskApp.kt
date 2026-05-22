package com.example.smarttaskmanager.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import com.example.smarttaskmanager.viewmodel.TaskViewModel

@Composable
fun TaskApp(
    taskViewModel: TaskViewModel = viewModel()
) {
    val navController = rememberNavController()
    val tasks = taskViewModel.tasks.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = "task_list"
    ) {
        composable("task_list") {
            TaskListScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = { navController.navigate("add_task") },
                onEditTaskClick = { taskId ->
                    navController.navigate("edit_task/$taskId")
                }
            )
        }

        composable("add_task") {
            AddEditTaskScreen(
                title = "Add Task",
                onSave = { task ->
                    taskViewModel.addTask(task)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("edit_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val task = tasks.find { it.id == taskId }

            AddEditTaskScreen(
                title = "Edit Task",
                initialTask = task,
                onSave = { updatedTask ->
                    taskViewModel.updateTask(updatedTask.copy(id = task?.id ?: 0))
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}