package edu.csuglobal.csc475.todone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodoScreen(tasks: List<Task> = emptyList()) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(task)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )
                PriorityIndicator(task.priority)
            }
            if (expanded) {
                TaskDetails(task)
                if (task.childTasks.isNotEmpty()) {
                    task.childTasks.forEach { childTask ->
                        TaskItem(childTask)
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityIndicator(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.NONE -> Color(0x0000FF)
        TaskPriority.OVERDUE_DEFERRED -> Color(0x808080)
        TaskPriority.LOW -> Color(0x008000)
        TaskPriority.MODERATE -> Color(0xFFFF00)
        TaskPriority.HIGH -> Color(0xFFA500)
        TaskPriority.VERY_HIGH -> Color(0xFF0000)
    }
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(color)
    )
}

@Composable
fun TaskDetails(task: Task) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Start Date: ${task.startDate}")
        Text("Estimated Time: ${task.estimatedTime} hours")
        Text("Due Date: ${task.dueDate}")
        Text("Description: ${task.description}")
        Text("Day Off Task: ${if (task.isDayOffTask) "Yes" else "No"}")
        Text("Materials Required: ${task.materialsRequired.joinToString(", ")}")
    }
}