package com.upstox.pulkitnigamtask.presentation.model

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary

sealed class HoldingsState {
    object Loading : HoldingsState()
    data class Success(
        val holdings: List<Holding>,
        val portfolioSummary: PortfolioSummary
    ) : HoldingsState()
    data class Error(val message: String) : HoldingsState()
}
