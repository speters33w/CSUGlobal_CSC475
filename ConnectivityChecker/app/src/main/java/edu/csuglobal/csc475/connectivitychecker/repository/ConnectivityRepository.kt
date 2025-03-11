import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.appdistribution.gradle.ApiService
import edu.csuglobal.csc475.connectivitychecker.ConnectivityTest
import kotlinx.coroutines.flow.Flow

class ConnectivityRepository(
    private val api: ApiService,
    private val db: AppDatabase,
    private val connectivityManager: ConnectivityManager
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagedTests(): Flow<PagingData<ConnectivityTest>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = ConnectivityTestRemoteMediator(api, db),
            pagingSourceFactory = { db.connectivityTestDao().getPagedTests() }
        ).flow
    }

    suspend fun prefetchData() {
        val recentTests = db.connectivityTestDao().getRecentTests(10)
        val predictedNextTest = predictNextTest(recentTests)
        val prefetchedData = api.fetchDataForTest(predictedNextTest)
        db.connectivityTestDao().insertPrefetchedData(prefetchedData)
    }

    private fun predictNextTest(recentTests: List<ConnectivityTest>): ConnectivityTest? {
        // Implement prediction logic here
        // For now, return a dummy ConnectivityTest
        return null
    }
    
        suspend fun fetchData() {
            when {
                isHighBandwidthConnection() -> fetchFullData()
                isLowBandwidthConnection() -> fetchLightweightData()
                else -> useLocalCache()
            }
        }
    
        private fun isHighBandwidthConnection(): Boolean {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    
        private fun isLowBandwidthConnection(): Boolean {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    
        private suspend fun fetchFullData() {
            // Fetch and store full data
        }
    
        private suspend fun fetchLightweightData() {
            // Fetch and store lightweight data
        }
    
        private suspend fun useLocalCache() {
            // Use locally cached data
        }
    }
}