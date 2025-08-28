package com.upstox.pulkitnigamtask.data.local.dao

import androidx.room.*
import com.upstox.pulkitnigamtask.data.local.entity.HoldingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Holding entity.
 * Provides database operations for stock holdings.
 */
@Dao
interface HoldingDao {
    
    /**
     * Get all holdings as a Flow for reactive updates.
     */
    @Query("SELECT * FROM holdings ORDER BY symbol ASC")
    fun getAllHoldings(): Flow<List<HoldingEntity>>
    
    /**
     * Get all holdings as a List.
     */
    @Query("SELECT * FROM holdings ORDER BY symbol ASC")
    suspend fun getAllHoldingsList(): List<HoldingEntity>
    
    /**
     * Get a specific holding by symbol.
     */
    @Query("SELECT * FROM holdings WHERE symbol = :symbol")
    suspend fun getHoldingBySymbol(symbol: String): HoldingEntity?
    
    /**
     * Insert a single holding.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolding(holding: HoldingEntity)
    
    /**
     * Insert multiple holdings.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<HoldingEntity>)
    
    /**
     * Update a holding.
     */
    @Update
    suspend fun updateHolding(holding: HoldingEntity)
    
    /**
     * Delete a holding by symbol.
     */
    @Query("DELETE FROM holdings WHERE symbol = :symbol")
    suspend fun deleteHoldingBySymbol(symbol: String)
    
    /**
     * Delete all holdings.
     */
    @Query("DELETE FROM holdings")
    suspend fun deleteAllHoldings()
    
    /**
     * Get total count of holdings.
     */
    @Query("SELECT COUNT(*) FROM holdings")
    suspend fun getHoldingsCount(): Int
    
    /**
     * Get holdings with positive P&L.
     */
    @Query("SELECT * FROM holdings WHERE (ltp - avgPrice) * quantity > 0 ORDER BY symbol ASC")
    fun getProfitableHoldings(): Flow<List<HoldingEntity>>
    
    /**
     * Get holdings with negative P&L.
     */
    @Query("SELECT * FROM holdings WHERE (ltp - avgPrice) * quantity < 0 ORDER BY symbol ASC")
    fun getLossMakingHoldings(): Flow<List<HoldingEntity>>
}
