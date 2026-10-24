package com.ayumi.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        RoutineSlot::class,
        DayOverride::class,
        Completion::class,
        DayLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AyumiDatabase : RoomDatabase() {
    abstract fun routineSlotDao(): RoutineSlotDao
    abstract fun dayOverrideDao(): DayOverrideDao
    abstract fun completionDao(): CompletionDao
    abstract fun dayLogDao(): DayLogDao

    companion object {
        @Volatile
        private var INSTANCE: AyumiDatabase? = null

        fun getDatabase(context: Context): AyumiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AyumiDatabase::class.java,
                    "ayumi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
