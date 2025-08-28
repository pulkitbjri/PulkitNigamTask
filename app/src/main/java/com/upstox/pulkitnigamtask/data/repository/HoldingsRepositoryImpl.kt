package com.upstox.pulkitnigamtask.data.repository

import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.data.remote.dto.HoldingDto
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HoldingsRepository {

    override suspend fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        try {
            val response = apiService.getHoldings()
            val holdings = response.data.userHolding.map { it.toDomain() }
            emit(Result.success(holdings))
        } catch (e: Exception) {
            emit(Result.failure(e))
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
