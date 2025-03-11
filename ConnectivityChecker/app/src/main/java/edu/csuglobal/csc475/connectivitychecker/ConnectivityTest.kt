package edu.csuglobal.csc475.connectivitychecker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "connectivity_tests")
data class ConnectivityTest(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val dateTime: Date,
    val opensignalPingResult: Double,
    val speedtestPingResult: Double,
    val label: ConnectivityLabel
)