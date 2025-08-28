package com.upstox.pulkitnigamtask.domain.validator

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary

/**
 * Domain validator for business rule validation.
 * This follows Clean Architecture by keeping validation logic in the domain layer.
 */
object HoldingValidator {
    
    private const val MIN_QUANTITY = 0
    private const val MIN_PRICE = 0.0
    
    /**
     * Validates a holding according to business rules.
     * @param holding The holding to validate
     * @throws IllegalArgumentException if validation fails
     */
    fun validate(holding: Holding) {
        require(holding.symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(holding.quantity >= MIN_QUANTITY) { "Quantity cannot be negative" }
        require(holding.ltp >= MIN_PRICE) { "LTP cannot be negative" }
        require(holding.avgPrice >= MIN_PRICE) { "Average price cannot be negative" }
        require(holding.close >= MIN_PRICE) { "Close price cannot be negative" }
    }
    
    /**
     * Validates a portfolio summary according to business rules.
     * @param summary The portfolio summary to validate
     * @throws IllegalArgumentException if validation fails
     */
    fun validate(summary: PortfolioSummary) {
        require(summary.currentValue >= MIN_PRICE) { "Current value cannot be negative" }
        require(summary.totalInvestment >= MIN_PRICE) { "Total investment cannot be negative" }
    }
}
