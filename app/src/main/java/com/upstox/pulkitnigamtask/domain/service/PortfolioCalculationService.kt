package com.upstox.pulkitnigamtask.domain.service

import com.upstox.pulkitnigamtask.domain.exception.PortfolioCalculationException
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.validator.HoldingValidator
import javax.inject.Inject

/**
 * Domain service for portfolio calculations.
 * This service encapsulates all business logic related to portfolio calculations.
 * 
 * This follows Clean Architecture principles by keeping business logic in the domain layer.
 */
class PortfolioCalculationService @Inject constructor() {
    
    /**
     * Calculates portfolio summary from a list of holdings.
     * @param holdings List of holdings to calculate summary for
     * @return PortfolioSummary containing calculated values
     * @throws PortfolioCalculationException if calculation fails
     */
    fun calculatePortfolioSummary(holdings: List<Holding>): PortfolioSummary {
        try {
            // Validate holdings data
            holdings.forEach { HoldingValidator.validate(it) }
            
            val currentValue = calculateCurrentValue(holdings)
            val totalInvestment = calculateTotalInvestment(holdings)
            val totalPnl = calculateTotalPnl(currentValue, totalInvestment)
            val todaysPnl = calculateTodaysPnl(holdings)
            
            val summary = PortfolioSummary(
                currentValue = currentValue,
                totalInvestment = totalInvestment,
                totalPnl = totalPnl,
                todaysPnl = todaysPnl
            )
            
            // Validate summary
            HoldingValidator.validate(summary)
            
            return summary
        } catch (e: Exception) {
            throw PortfolioCalculationException("Failed to calculate portfolio summary", e)
        }
    }

    private fun calculateCurrentValue(holdings: List<Holding>): Double {
        return holdings.sumOf { it.ltp * it.quantity }
    }

    private fun calculateTotalInvestment(holdings: List<Holding>): Double {
        return holdings.sumOf { it.avgPrice * it.quantity }
    }

    private fun calculateTotalPnl(currentValue: Double, totalInvestment: Double): Double {
        return currentValue - totalInvestment
    }

    private fun calculateTodaysPnl(holdings: List<Holding>): Double {
        return holdings.sumOf { (it.close - it.ltp) * it.quantity }
    }
}
