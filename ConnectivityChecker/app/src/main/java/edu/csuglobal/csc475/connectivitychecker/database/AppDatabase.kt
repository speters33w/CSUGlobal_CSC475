import androidx.room.Database
import androidx.room.RoomDatabase
import edu.csuglobal.csc475.connectivitychecker.ConnectivityTest

@Database(entities = [ConnectivityTest::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun connectivityTestDao(): ConnectivityTestDao
}