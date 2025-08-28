package com.upstox.pulkitnigamtask.data.repository

import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import com.upstox.pulkitnigamtask.data.local.mapper.HoldingMapper
import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.data.remote.dto.HoldingDto
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val holdingDao: HoldingDao
) : HoldingsRepository {

    override suspend fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        try {
            val response = apiService.getHoldings()
            val holdings = response.data.userHolding.map { it.toDomain() }
            
            // Save to local database for caching
            saveHoldings(holdings)
            
            emit(Result.success(holdings))
        } catch (e: Exception) {
            // If API fails, try to get from local database
            try {
                val localHoldings = holdingDao.getAllHoldingsList()
                val domainHoldings = HoldingMapper.toDomainList(localHoldings)
                emit(Result.success(domainHoldings))
            } catch (dbException: Exception) {
                emit(Result.failure(e))
            }
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
