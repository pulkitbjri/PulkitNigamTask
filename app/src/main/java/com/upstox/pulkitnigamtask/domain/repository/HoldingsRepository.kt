package com.upstox.pulkitnigamtask.domain.repository

import com.upstox.pulkitnigamtask.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    /**
     * Get holdings from remote API with local caching.
     */
    suspend fun getHoldings(): Flow<Result<List<Holding>>>
    
    /**
     * Get holdings from local database only.
     */
    fun getLocalHoldings(): Flow<List<Holding>>
    
    /**
     * Save holdings to local database.
     */
    suspend fun saveHoldings(holdings: List<Holding>)
    
    /**
     * Clear all local holdings.
     */
    suspend fun clearLocalHoldings()
    
    /**
     * Get a specific holding by symbol from local database.
     */
    suspend fun getHoldingBySymbol(symbol: String): Holding?
    
    /**
     * Get profitable holdings from local database.
     */
    fun getProfitableHoldings(): Flow<List<Holding>>
    
    /**
     * Get loss-making holdings from local database.
     */
    fun getLossMakingHoldings(): Flow<List<Holding>>
}
