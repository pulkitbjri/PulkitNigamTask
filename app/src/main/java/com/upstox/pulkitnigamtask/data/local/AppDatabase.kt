package com.upstox.pulkitnigamtask.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import com.upstox.pulkitnigamtask.data.local.entity.HoldingEntity

/**
 * Main Room database class for the application.
 * Contains all entities and provides access to DAOs.
 */
@Database(
    entities = [HoldingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Get the HoldingDao for database operations.
     */
    abstract fun holdingDao(): HoldingDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Get the database instance (Singleton pattern).
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pulkit_nigam_task_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
