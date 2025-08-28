package com.upstox.pulkitnigamtask.di.modules.data

import android.content.Context
import com.upstox.pulkitnigamtask.data.local.AppDatabase
import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing database-related dependencies.
 * Provides Room database instance and DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides the Room database instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    /**
     * Provides the HoldingDao for database operations.
     */
    @Provides
    @Singleton
    fun provideHoldingDao(database: AppDatabase): HoldingDao {
        return database.holdingDao()
    }
}
