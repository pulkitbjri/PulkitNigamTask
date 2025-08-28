package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching portfolio holdings from the repository.
 * This use case encapsulates the business logic for retrieving holdings data.
 */
class GetHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository
) {
    /**
     * Invokes the use case to fetch holdings data.
     * @return Flow of Result containing list of holdings or error
     */
    suspend operator fun invoke(): Flow<Result<List<Holding>>> = repository.getHoldings()
}


