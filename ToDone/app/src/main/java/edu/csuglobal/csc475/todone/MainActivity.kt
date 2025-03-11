package edu.csuglobal.csc475.todone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoneApp()
        }
    }
}

@Composable
fun ToDoneApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "todo",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("todo") { TodoScreen() }
            composable("done") { DoneScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun TopAppBar() {
    TabRow(selectedTabIndex = 0) {
        Tab(selected = true, onClick = { /* TODO */ }) {
            Text("ToDo")
        }
        Tab(selected = false, onClick = { /* TODO */ }) {
            Text("Done")
        }
        Tab(selected = false, onClick = { /* TODO */ }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.ArrowUpward, contentDescription = "Task Details") },
            label = { Text("Details") },
            selected = false,
            onClick = { /* TODO: Open task details dialog */ }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
            label = { Text("Add") },
            selected = false,
            onClick = { /* TODO: Add new task */ }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Close, contentDescription = "Exit") },
            label = { Text("Exit") },
            selected = false,
            onClick = { /* TODO: Exit application */ }
        )
    }
}



@Composable
fun TodoScreen() {
    // TODO: Implement TodoScreen
}

@Composable
fun DoneScreen() {
    // TODO: Implement DoneScreen
}

@Composable
fun SettingsScreen() {
    // TODO: Implement SettingsScreen
}