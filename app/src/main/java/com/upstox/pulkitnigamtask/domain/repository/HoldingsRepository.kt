package com.upstox.pulkitnigamtask.domain.repository

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    suspend fun getHoldings(): Flow<Result<List<Holding>>>
}
