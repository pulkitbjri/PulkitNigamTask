package com.upstox.pulkitnigamtask.data.local

import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import com.upstox.pulkitnigamtask.data.local.mapper.HoldingMapper
import com.upstox.pulkitnigamtask.domain.model.Holding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Database helper utility class that provides convenience methods
 * for common database operations.
 */
@Singleton
class DatabaseHelper @Inject constructor(
    private val holdingDao: HoldingDao
) {
    
    /**
     * Get all holdings as a Flow.
     */
    fun getAllHoldings(): Flow<List<Holding>> {
        return holdingDao.getAllHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }
    
    /**
     * Get profitable holdings.
     */
    fun getProfitableHoldings(): Flow<List<Holding>> {
        return holdingDao.getProfitableHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }
    
    /**
     * Get loss-making holdings.
     */
    fun getLossMakingHoldings(): Flow<List<Holding>> {
        return holdingDao.getLossMakingHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }
    
    /**
     * Save holdings to database.
     */
    suspend fun saveHoldings(holdings: List<Holding>) {
        val entities = HoldingMapper.toEntityList(holdings)
        holdingDao.insertHoldings(entities)
    }
    
    /**
     * Clear all holdings from database.
     */
    suspend fun clearAllHoldings() {
        holdingDao.deleteAllHoldings()
    }
    
    /**
     * Get holding count.
     */
    suspend fun getHoldingsCount(): Int {
        return holdingDao.getHoldingsCount()
    }
    
    /**
     * Check if database is empty.
     */
    suspend fun isDatabaseEmpty(): Boolean {
        return holdingDao.getHoldingsCount() == 0
    }
}
