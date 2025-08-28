package com.upstox.pulkitnigamtask.di.modules.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger Hilt module for providing database-related dependencies.
 * This module is prepared for future Room database integration.
 * 
 * Future implementations may include:
 * - Room database instance
 * - DAO (Data Access Object) implementations
 * - Database migration strategies
 * - Database configuration
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Database dependencies will be added here when Room is integrated
    // Example:
    // @Provides
    // @Singleton
    // fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
    //     return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    // }
}
