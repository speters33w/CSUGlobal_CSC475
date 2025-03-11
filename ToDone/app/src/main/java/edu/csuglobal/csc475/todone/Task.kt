package edu.csuglobal.csc475.todone

import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val startDate: String,
    val estimatedTime: Double,
    val dueDate: String,
    val description: String,
    val isDayOffTask: Boolean,
    val materialsRequired: List<String>,
    val priority: TaskPriority,
    val childTasks: List<Task> = emptyList()
)

enum class TaskPriority {
    NONE,
    OVERDUE_DEFERRED,
    LOW,
    MODERATE,
    HIGH,
    VERY_HIGH
}