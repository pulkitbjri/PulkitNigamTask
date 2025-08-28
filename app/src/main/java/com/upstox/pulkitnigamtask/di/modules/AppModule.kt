package com.upstox.pulkitnigamtask.di.modules

import android.content.Context
import com.upstox.pulkitnigamtask.di.modules.data.DatabaseModule
import com.upstox.pulkitnigamtask.di.modules.data.NetworkModule
import com.upstox.pulkitnigamtask.di.modules.data.RepositoryModule
import com.upstox.pulkitnigamtask.di.modules.domain.UseCaseModule
import com.upstox.pulkitnigamtask.util.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    
    /**
     * Provides NetworkUtils for checking internet connectivity.
     */
    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}
