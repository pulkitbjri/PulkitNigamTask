package com.upstox.pulkitnigamtask.data.repository

import android.util.Log
import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import com.upstox.pulkitnigamtask.data.local.mapper.HoldingMapper
import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.data.remote.dto.HoldingDto
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val holdingDao: HoldingDao
) : HoldingsRepository {

    companion object {
        private const val TAG = "HoldingsRepository"
    }

    override suspend fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        Log.d(TAG, "getHoldings() called - making API request")
        try {
            Log.d(TAG, "Calling API service...")
            val response = apiService.getHoldings()
            Log.d(TAG, "API response received successfully")
            Log.d(TAG, "Response data: ${response.data}")
            Log.d(TAG, "User holdings count: ${response.data.userHolding.size}")
            
            val holdings = response.data.userHolding.map { it.toDomain() }
            Log.d(TAG, "Converted to domain models: ${holdings.size} holdings")
            
            // Save to local database for caching
            Log.d(TAG, "Saving ${holdings.size} holdings to local database")
            saveHoldings(holdings)
            
            Log.d(TAG, "Emitting success result with ${holdings.size} holdings")
            emit(Result.success(holdings))
        } catch (e: Exception) {
            Log.e(TAG, "API call failed with exception", e)
            Log.e(TAG, "Exception message: ${e.message}")
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            throw e
        }
    }.catch { e ->
        Log.e(TAG, "Flow caught exception, trying local database", e)
        // If API fails, try to get from local database
        try {
            val localHoldings = holdingDao.getAllHoldingsList()
            Log.d(TAG, "Retrieved ${localHoldings.size} holdings from local database")
            val domainHoldings = HoldingMapper.toDomainList(localHoldings)
            Log.d(TAG, "Emitting local data result with ${domainHoldings.size} holdings")
            emit(Result.success(domainHoldings))
        } catch (dbException: Exception) {
            Log.e(TAG, "Local database also failed", dbException)
            emit(Result.failure(e))
        }
    }

    override fun getLocalHoldings(): Flow<List<Holding>> {
        return holdingDao.getAllHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }

    override suspend fun saveHoldings(holdings: List<Holding>) {
        val entities = HoldingMapper.toEntityList(holdings)
        holdingDao.insertHoldings(entities)
    }

    override suspend fun clearLocalHoldings() {
        holdingDao.deleteAllHoldings()
    }

    override suspend fun getHoldingBySymbol(symbol: String): Holding? {
        val entity = holdingDao.getHoldingBySymbol(symbol)
        return entity?.let { HoldingMapper.toDomain(it) }
    }

    override fun getProfitableHoldings(): Flow<List<Holding>> {
        return holdingDao.getProfitableHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }

    override fun getLossMakingHoldings(): Flow<List<Holding>> {
        return holdingDao.getLossMakingHoldings().map { entities ->
            HoldingMapper.toDomainList(entities)
        }
    }

    private fun HoldingDto.toDomain(): Holding {
        return Holding(
            symbol = symbol,
            quantity = quantity,
            ltp = ltp,
            avgPrice = avgPrice,
            close = close
        )
    }
}
