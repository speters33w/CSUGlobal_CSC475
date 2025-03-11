package edu.csuglobal.csc475.connectivitychecker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(entities = [ConnectivityTest::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ConnectivityDatabase : RoomDatabase() {
    abstract fun connectivityDao(): ConnectivityDao

    companion object {
        @Volatile
        private var INSTANCE: ConnectivityDatabase? = null

        fun getDatabase(context: Context): ConnectivityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConnectivityDatabase::class.java,
                    "connectivity_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toConnectivityLabel(value: String) = enumValueOf<ConnectivityLabel>(value)

    @TypeConverter
    fun fromConnectivityLabel(value: ConnectivityLabel) = value.name
}