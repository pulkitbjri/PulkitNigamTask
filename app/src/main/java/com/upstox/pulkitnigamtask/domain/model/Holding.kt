package com.upstox.pulkitnigamtask.domain.model

/**
 * Domain model representing a single stock holding in the portfolio.
 * Contains all the essential data for a stock position.
 * 
 * This model is completely independent of other layers, following Clean Architecture principles.
 */
data class Holding(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
) {
    /**
     * Calculates the total P&L for this holding.
     * Formula: (LTP - Average Price) × Quantity
     */
    val pnl: Double
        get() = (ltp - avgPrice) * quantity
    
    /**
     * Calculates today's P&L for this holding.
     * Formula: (Close - LTP) × Quantity
     */
    val todaysPnl: Double
        get() = (close - ltp) * quantity

    /**
     * Calculates the current value of this holding.
     * Formula: LTP × Quantity
     */
    val currentValue: Double
        get() = ltp * quantity

    /**
     * Calculates the total investment in this holding.
     * Formula: Average Price × Quantity
     */
    val totalInvestment: Double
        get() = avgPrice * quantity
}

/**
 * Domain model representing the portfolio summary with calculated values.
 * Contains aggregated portfolio statistics.
 */
data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnl: Double,
    val todaysPnl: Double
)
