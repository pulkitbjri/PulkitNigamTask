package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.domain.service.PortfolioCalculationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Combined use case for fetching holdings and calculating portfolio summary.
 * This use case encapsulates the complete business logic for portfolio data retrieval.
 * 
 * This follows Clean Architecture by keeping business logic in the domain layer
 * and follows Single Responsibility Principle by having one clear responsibility.
 */
class GetHoldingsWithSummaryUseCase @Inject constructor(
    private val repository: HoldingsRepository,
    private val portfolioCalculationService: PortfolioCalculationService
) {
    /**
     * Invokes the use case to fetch holdings and calculate summary.
     * @return Flow of Result containing holdings with calculated summary or error
     */
    suspend operator fun invoke(): Flow<Result<HoldingsWithSummary>> = 
        repository.getHoldings().map { result ->
            result.map { holdings ->
                val summary = portfolioCalculationService.calculatePortfolioSummary(holdings)
                HoldingsWithSummary(holdings, summary)
            }
        }
}

/**
 * Data class representing holdings with calculated portfolio summary.
 * This encapsulates the complete portfolio data needed by the presentation layer.
 */
data class HoldingsWithSummary(
    val holdings: List<Holding>,
    val portfolioSummary: PortfolioSummary
)
