package com.upstox.pulkitnigamtask.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utility class for formatting values used throughout the application.
 * Provides consistent formatting for currency, percentages, and other values.
 */
object FormattingUtils {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    private val percentageFormatter = NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    /**
     * Formats a double value as Indian Rupees currency.
     * @param value The value to format
     * @return Formatted currency string (e.g., "₹1,234.56")
     */
    fun formatCurrency(value: Double): String {
        return currencyFormatter.format(value)
    }

    /**
     * Formats a double value as a percentage.
     * @param value The value to format (should be in decimal form, e.g., 0.123 for 12.3%)
     * @return Formatted percentage string (e.g., "12.30%")
     */
    fun formatPercentage(value: Double): String {
        return percentageFormatter.format(value)
    }

    /**
     * Formats P&L with percentage for display.
     * @param pnl The P&L value
     * @param investment The investment value for percentage calculation
     * @return Formatted string (e.g., "₹1,234.56 (12.30%)")
     */
    fun formatPnlWithPercentage(pnl: Double, investment: Double): String {
        val percentage = if (investment > 0) (pnl / investment) else 0.0
        return "${formatCurrency(pnl)} (${formatPercentage(percentage)})"
    }

    /**
     * Formats a quantity value for display.
     * @param quantity The quantity value
     * @return Formatted quantity string
     */
    fun formatQuantity(quantity: Int): String {
        return "NET QTY: $quantity"
    }

    /**
     * Formats LTP (Last Traded Price) for display.
     * @param ltp The LTP value
     * @return Formatted LTP string (e.g., "LTP: ₹1,234.56")
     */
    fun formatLtp(ltp: Double): String {
        return "LTP: ${formatCurrency(ltp)}"
    }

    /**
     * Formats P&L for display.
     * @param pnl The P&L value
     * @return Formatted P&L string (e.g., "P&L: ₹1,234.56")
     */
    fun formatPnl(pnl: Double): String {
        return "P&L: ${formatCurrency(pnl)}"
    }
}
