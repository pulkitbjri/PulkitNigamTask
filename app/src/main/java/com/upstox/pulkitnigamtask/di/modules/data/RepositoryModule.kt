package com.upstox.pulkitnigamtask.di.modules.data

import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.data.repository.HoldingsRepositoryImpl
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing repository implementations.
 * Maps repository interfaces to their concrete implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides HoldingsRepository implementation.
     * Maps the interface to its concrete implementation for dependency injection.
     */
    @Provides
    @Singleton
    fun provideHoldingsRepository(
        apiService: ApiService,
        holdingDao: HoldingDao
    ): HoldingsRepository {
        return HoldingsRepositoryImpl(apiService, holdingDao)
    }
}
