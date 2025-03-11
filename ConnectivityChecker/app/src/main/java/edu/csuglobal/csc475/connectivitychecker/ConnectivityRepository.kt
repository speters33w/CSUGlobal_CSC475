package edu.csuglobal.csc475.connectivitychecker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class ConnectivityRepository(private val connectivityDao: ConnectivityDao) {
    private val dbUrl = "jdbc:mysql://192.168.1.100:3306/csc475_connectivity_test"
    private val dbUser = "your_username"
    private val dbPassword = "your_password"

    suspend fun insertTest(test: ConnectivityTest) {
        connectivityDao.insertTest(test)
        syncWithRemoteDb()
    }

    suspend fun getAllTests(): List<ConnectivityTest> {
        return connectivityDao.getAllTests()
    }

    suspend fun clearAllTests() {
        connectivityDao.clearAllTests()
        clearRemoteDb()
    }

    suspend fun testConnectivity(): ConnectivityTest {
        return withContext(Dispatchers.IO) {
            val opensignalPing = pingHost("www.opensignal.com")
            val speedtestPing = pingHost("www.speedtest.net")
            
            val avgSpeed = (opensignalPing + speedtestPing) / 2
            val label = when {
                opensignalPing == 0.0 && speedtestPing == 0.0 -> ConnectivityLabel.NONE
                avgSpeed < 1 -> ConnectivityLabel.POOR
                avgSpeed < 3 -> ConnectivityLabel.SLOW
                avgSpeed < 5 -> ConnectivityLabel.OK
                avgSpeed < 20 -> ConnectivityLabel.GOOD
                else -> ConnectivityLabel.EXCELLENT
            }

            ConnectivityTest(
                dateTime = Date(),
                opensignalPingResult = opensignalPing,
                speedtestPingResult = speedtestPing,
                label = label
            )
        }
    }

    private fun pingHost(host: String): Double {
        return try {
            val start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(host).isReachable(5000)
            val end = System.currentTimeMillis()
            if (reachable) (end - start).toDouble() else 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    suspend fun syncWithRemoteDb() {
        withContext(Dispatchers.IO) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                DriverManager.getConnection(dbUrl, dbUser, dbPassword).use { connection ->
                    createTableIfNotExists(connection)
                    val tests = connectivityDao.getAllTests()
                    for (test in tests) {
                        insertTestToRemoteDb(connection, test)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createTableIfNotExists(connection: Connection) {
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS connectivity_tests (
                id VARCHAR(36) PRIMARY KEY,
                dateTime DATETIME,
                opensignalPingResult DOUBLE,
                speedtestPingResult DOUBLE,
                label VARCHAR(20)
            )
        """.trimIndent()
        connection.createStatement().use { it.execute(createTableSQL) }
    }

    private fun insertTestToRemoteDb(connection: Connection, test: ConnectivityTest) {
        val insertSQL = """
            INSERT INTO connectivity_tests (id, dateTime, opensignalPingResult, speedtestPingResult, label)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            dateTime = VALUES(dateTime),
            opensignalPingResult = VALUES(opensignalPingResult),
            speedtestPingResult = VALUES(speedtestPingResult),
            label = VALUES(label)
        """.trimIndent()
        connection.prepareStatement(insertSQL).use { statement ->
            statement.setString(1, test.id.toString())
            statement.setTimestamp(2, java.sql.Timestamp(test.dateTime.time))
            statement.setDouble(3, test.opensignalPingResult)
            statement.setDouble(4, test.speedtestPingResult)
            statement.setString(5, test.label.name)
            statement.executeUpdate()
        }
    }

    private suspend fun clearRemoteDb() {
        withContext(Dispatchers.IO) {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                DriverManager.getConnection(dbUrl, dbUser, dbPassword).use { connection ->
                    val clearSQL = "DELETE FROM connectivity_tests"
                    connection.createStatement().use { it.execute(clearSQL) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}