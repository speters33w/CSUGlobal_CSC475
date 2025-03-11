package edu.csuglobal.csc475.connectivitychecker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConnectivityRepository
    private val _tests = MutableStateFlow<List<ConnectivityTest>>(emptyList())
    val tests: StateFlow<List<ConnectivityTest>> = _tests

    init {
        val database = ConnectivityDatabase.getDatabase(application)
        repository = ConnectivityRepository(database.connectivityDao())
        loadTests()
        startPeriodicSync()
    }

    private fun loadTests() {
        viewModelScope.launch {
            _tests.value = repository.getAllTests()
        }
    }

    fun testConnectivity() {
        viewModelScope.launch {
            val test = repository.testConnectivity()
            repository.insertTest(test)
            loadTests()
        }
    }

    fun clearLog() {
        viewModelScope.launch {
            repository.clearAllTests()
            loadTests()
        }
    }

    private fun startPeriodicSync() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10 * 60 * 1000) // 10 minutes
                if (_tests.value.isNotEmpty()) {
                    repository.syncWithRemoteDb()
                }
            }
        }
    }
}