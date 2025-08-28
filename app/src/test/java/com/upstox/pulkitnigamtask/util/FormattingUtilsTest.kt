package com.upstox.pulkitnigamtask.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattingUtilsTest {

    @Test
    fun `formatCurrency should format positive values correctly`() {
        // Given
        val value = 1234.56

        // When
        val result = FormattingUtils.formatCurrency(value)

        // Then
        assertEquals("₹1,234.56", result)
    }

    @Test
    fun `formatCurrency should format zero correctly`() {
        // Given
        val value = 0.0

        // When
        val result = FormattingUtils.formatCurrency(value)

        // Then
        assertEquals("₹0.00", result)
    }

    @Test
    fun `formatCurrency should format negative values correctly`() {
        // Given
        val value = -1234.56

        // When
        val result = FormattingUtils.formatCurrency(value)

        // Then
        assertEquals("-₹1,234.56", result)
    }

    @Test
    fun `formatCurrency should format large values correctly`() {
        // Given
        val value = 1234567.89

        // When
        val result = FormattingUtils.formatCurrency(value)

        // Then
        assertEquals("₹1,234,567.89", result)
    }

    @Test
    fun `formatCurrency should format decimal values correctly`() {
        // Given
        val value = 100.5

        // When
        val result = FormattingUtils.formatCurrency(value)

        // Then
        assertEquals("₹100.50", result)
    }

    @Test
    fun `formatPercentage should format positive percentages correctly`() {
        // Given
        val value = 0.1234 // 12.34%

        // When
        val result = FormattingUtils.formatPercentage(value)

        // Then
        assertEquals("12.34%", result)
    }

    @Test
    fun `formatPercentage should format zero percentage correctly`() {
        // Given
        val value = 0.0

        // When
        val result = FormattingUtils.formatPercentage(value)

        // Then
        assertEquals("0.00%", result)
    }

    @Test
    fun `formatPercentage should format negative percentages correctly`() {
        // Given
        val value = -0.1234 // -12.34%

        // When
        val result = FormattingUtils.formatPercentage(value)

        // Then
        assertEquals("-12.34%", result)
    }

    @Test
    fun `formatPercentage should format large percentages correctly`() {
        // Given
        val value = 1.5 // 150%

        // When
        val result = FormattingUtils.formatPercentage(value)

        // Then
        assertEquals("150.00%", result)
    }

    @Test
    fun `formatPercentage should format small percentages correctly`() {
        // Given
        val value = 0.001 // 0.1%

        // When
        val result = FormattingUtils.formatPercentage(value)

        // Then
        assertEquals("0.10%", result)
    }

    @Test
    fun `formatPnlWithPercentage should format positive P&L correctly`() {
        // Given
        val pnl = 1000.0
        val investment = 10000.0

        // When
        val result = FormattingUtils.formatPnlWithPercentage(pnl, investment)

        // Then
        assertEquals("₹1,000.00 (10.00%)", result)
    }

    @Test
    fun `formatPnlWithPercentage should format negative P&L correctly`() {
        // Given
        val pnl = -500.0
        val investment = 10000.0

        // When
        val result = FormattingUtils.formatPnlWithPercentage(pnl, investment)

        // Then
        assertEquals("-₹500.00 (-5.00%)", result)
    }

    @Test
    fun `formatPnlWithPercentage should handle zero investment`() {
        // Given
        val pnl = 1000.0
        val investment = 0.0

        // When
        val result = FormattingUtils.formatPnlWithPercentage(pnl, investment)

        // Then
        assertEquals("₹1,000.00 (0.00%)", result)
    }

    @Test
    fun `formatPnlWithPercentage should handle zero P&L`() {
        // Given
        val pnl = 0.0
        val investment = 10000.0

        // When
        val result = FormattingUtils.formatPnlWithPercentage(pnl, investment)

        // Then
        assertEquals("₹0.00 (0.00%)", result)
    }

    @Test
    fun `formatPnlWithPercentage should handle large values correctly`() {
        // Given
        val pnl = 50000.0
        val investment = 100000.0

        // When
        val result = FormattingUtils.formatPnlWithPercentage(pnl, investment)

        // Then
        assertEquals("₹50,000.00 (50.00%)", result)
    }

    @Test
    fun `formatQuantity should format quantity correctly`() {
        // Given
        val quantity = 100

        // When
        val result = FormattingUtils.formatQuantity(quantity)

        // Then
        assertEquals("NET QTY: 100", result)
    }

    @Test
    fun `formatQuantity should format zero quantity correctly`() {
        // Given
        val quantity = 0

        // When
        val result = FormattingUtils.formatQuantity(quantity)

        // Then
        assertEquals("NET QTY: 0", result)
    }

    @Test
    fun `formatQuantity should format negative quantity correctly`() {
        // Given
        val quantity = -50

        // When
        val result = FormattingUtils.formatQuantity(quantity)

        // Then
        assertEquals("NET QTY: -50", result)
    }

    @Test
    fun `formatLtp should format LTP correctly`() {
        // Given
        val ltp = 2500.0

        // When
        val result = FormattingUtils.formatLtp(ltp)

        // Then
        assertEquals("LTP: ₹2,500.00", result)
    }

    @Test
    fun `formatLtp should format zero LTP correctly`() {
        // Given
        val ltp = 0.0

        // When
        val result = FormattingUtils.formatLtp(ltp)

        // Then
        assertEquals("LTP: ₹0.00", result)
    }

    @Test
    fun `formatLtp should format decimal LTP correctly`() {
        // Given
        val ltp = 1234.56

        // When
        val result = FormattingUtils.formatLtp(ltp)

        // Then
        assertEquals("LTP: ₹1,234.56", result)
    }

    @Test
    fun `formatPnl should format positive P&L correctly`() {
        // Given
        val pnl = 1000.0

        // When
        val result = FormattingUtils.formatPnl(pnl)

        // Then
        assertEquals("P&L: ₹1,000.00", result)
    }

    @Test
    fun `formatPnl should format negative P&L correctly`() {
        // Given
        val pnl = -500.0

        // When
        val result = FormattingUtils.formatPnl(pnl)

        // Then
        assertEquals("P&L: -₹500.00", result)
    }

    @Test
    fun `formatPnl should format zero P&L correctly`() {
        // Given
        val pnl = 0.0

        // When
        val result = FormattingUtils.formatPnl(pnl)

        // Then
        assertEquals("P&L: ₹0.00", result)
    }

    @Test
    fun `formatPnl should format decimal P&L correctly`() {
        // Given
        val pnl = 1234.56

        // When
        val result = FormattingUtils.formatPnl(pnl)

        // Then
        assertEquals("P&L: ₹1,234.56", result)
    }

    @Test
    fun `formatPnl should format large P&L correctly`() {
        // Given
        val pnl = 1234567.89

        // When
        val result = FormattingUtils.formatPnl(pnl)

        // Then
        assertEquals("P&L: ₹1,234,567.89", result)
    }
}
