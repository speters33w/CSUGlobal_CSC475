package edu.csuglobal.csc475.connectivitychecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ConnectivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConnectivityViewModel::class.java)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConnectivityCheckerApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun ConnectivityCheckerApp(viewModel: ConnectivityViewModel) {
    val tests by viewModel.tests.collectAsState()
    var showLogDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.testConnectivity() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Test")
        }

        Button(
            onClick = { showLogDialog = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Log")
        }

        Button(
            onClick = { viewModel.clearLog() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Clear Log")
        }

        if (showLogDialog) {
            AlertDialog(
                onDismissRequest = { showLogDialog = false },
                title = { Text("Connectivity Log") },
                text = {
                    LazyColumn {
                        items(tests) { test ->
                            TestItem(test)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showLogDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun TestItem(test: ConnectivityTest) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${dateFormat.format(test.dateTime)}")
            Text("OpenSignal Ping: ${test.opensignalPingResult} ms")
            Text("Speedtest Ping: ${test.speedtestPingResult} ms")
            Text("Label: ${test.label.label}")
        }
    }
}