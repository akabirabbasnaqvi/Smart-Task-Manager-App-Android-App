package com.example.smarttaskmanager.data

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks() = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}