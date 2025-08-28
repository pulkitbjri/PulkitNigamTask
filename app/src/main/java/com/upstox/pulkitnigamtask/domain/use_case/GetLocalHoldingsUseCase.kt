package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching holdings from local database only.
 * This use case is useful for offline scenarios or when you want to
 * access cached data without making network requests.
 */
class GetLocalHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to fetch holdings from local database.
     * @return Flow of holdings from local database
     */
    operator fun invoke(): Flow<List<Holding>> = repository.getLocalHoldings()
}

/**
 * Use case for fetching profitable holdings from local database.
 */
class GetProfitableHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to fetch profitable holdings from local database.
     * @return Flow of profitable holdings
     */
    operator fun invoke(): Flow<List<Holding>> = repository.getProfitableHoldings()
}

/**
 * Use case for fetching loss-making holdings from local database.
 */
class GetLossMakingHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to fetch loss-making holdings from local database.
     * @return Flow of loss-making holdings
     */
    operator fun invoke(): Flow<List<Holding>> = repository.getLossMakingHoldings()
}

/**
 * Use case for saving holdings to local database.
 */
class SaveHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to save holdings to local database.
     * @param holdings List of holdings to save
     */
    suspend operator fun invoke(holdings: List<Holding>) {
        repository.saveHoldings(holdings)
    }
}

/**
 * Use case for clearing all holdings from local database.
 */
class ClearLocalHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to clear all holdings from local database.
     */
    suspend operator fun invoke() {
        repository.clearLocalHoldings()
    }
}
