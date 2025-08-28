package com.upstox.pulkitnigamtask.presentation.helper

import android.content.Context
import androidx.core.content.ContextCompat
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.util.FormattingUtils

/**
 * UI helper for portfolio-related formatting and display logic.
 * This follows separation of concerns by moving UI logic out of the Activity.
 */
class PortfolioUIHelper(private val context: Context) {

    /**
     * Formats portfolio summary values for display.
     * @param summary The portfolio summary to format
     * @return FormattedPortfolioSummary containing formatted strings
     */
    fun formatPortfolioSummary(summary: PortfolioSummary): FormattedPortfolioSummary {
        return FormattedPortfolioSummary(
            currentValue = FormattingUtils.formatCurrency(summary.currentValue),
            totalInvestment = FormattingUtils.formatCurrency(summary.totalInvestment),
            todaysPnl = FormattingUtils.formatCurrency(summary.todaysPnl),
            totalPnl = FormattingUtils.formatPnlWithPercentage(summary.totalPnl, summary.totalInvestment),
            todaysPnlColor = getPnlColor(summary.todaysPnl),
            totalPnlColor = getPnlColor(summary.totalPnl)
        )
    }

    /**
     * Gets the appropriate color for P&L values.
     * @param pnl The P&L value
     * @return Color resource ID
     */
    private fun getPnlColor(pnl: Double): Int {
        val colorRes = if (pnl >= 0) android.R.color.holo_green_dark else android.R.color.holo_red_dark
        return ContextCompat.getColor(context, colorRes)
    }
}

/**
 * Data class containing formatted portfolio summary values.
 * This encapsulates all the formatted strings needed for UI display.
 */
data class FormattedPortfolioSummary(
    val currentValue: String,
    val totalInvestment: String,
    val todaysPnl: String,
    val totalPnl: String,
    val todaysPnlColor: Int,
    val totalPnlColor: Int
)
