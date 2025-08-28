package com.upstox.pulkitnigamtask.di.modules.domain

import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.domain.service.PortfolioCalculationService
import com.upstox.pulkitnigamtask.domain.use_case.GetHoldingsWithSummaryUseCase
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

    /**
     * Provides GetHoldingsWithSummaryUseCase for fetching holdings and calculating summary.
     * This use case handles the complete business logic for portfolio data retrieval.
     */
    @Provides
    @Singleton
    fun provideGetHoldingsWithSummaryUseCase(
        repository: HoldingsRepository,
        portfolioCalculationService: PortfolioCalculationService
    ): GetHoldingsWithSummaryUseCase {
        return GetHoldingsWithSummaryUseCase(repository, portfolioCalculationService)
    }
}
