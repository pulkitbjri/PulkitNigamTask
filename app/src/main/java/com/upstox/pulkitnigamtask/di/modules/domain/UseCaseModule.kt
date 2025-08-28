package com.upstox.pulkitnigamtask.di.modules.domain

import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing use case dependencies.
 * Contains all business logic use cases and their dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // All use cases are now provided directly in their respective modules
}
