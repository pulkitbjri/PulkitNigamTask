package com.upstox.pulkitnigamtask.di.modules

import com.upstox.pulkitnigamtask.di.modules.data.DatabaseModule
import com.upstox.pulkitnigamtask.di.modules.data.NetworkModule
import com.upstox.pulkitnigamtask.di.modules.data.RepositoryModule
import com.upstox.pulkitnigamtask.di.modules.domain.UseCaseModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Main application module for Dagger Hilt.
 * This module serves as the entry point for all other modules.
 * 
 * Note: Most dependencies are now provided by specialized modules:
 * - NetworkModule: Networking dependencies
 * - RepositoryModule: Repository implementations
 * - UseCaseModule: Use case dependencies
 * - DatabaseModule: Database dependencies
 */
@Module(
    includes = [
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        DatabaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {
    // This module now serves as a coordinator for other modules
    // All specific dependencies are provided by the included modules
}
