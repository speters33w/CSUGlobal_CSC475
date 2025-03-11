package edu.csuglobal.csc475.connectivitychecker

import androidx.room.*

@Dao
interface ConnectivityDao {
    @Query("SELECT * FROM connectivity_tests ORDER BY dateTime DESC")
    suspend fun getAllTests(): List<ConnectivityTest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: ConnectivityTest)

    @Query("DELETE FROM connectivity_tests")
    suspend fun clearAllTests()
}