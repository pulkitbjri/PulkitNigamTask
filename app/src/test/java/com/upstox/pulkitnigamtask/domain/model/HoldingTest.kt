package com.upstox.pulkitnigamtask.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HoldingTest {

    @Test
    fun `pnl calculation should be correct for profitable holding`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val pnl = holding.pnl

        // Then
        val expectedPnl = (2500.0 - 2400.0) * 100 // (LTP - AvgPrice) * Quantity
        assertEquals(expectedPnl, pnl, 0.1)
        assertEquals(10000.0, pnl, 0.1) // Should be positive (profitable)
    }

    @Test
    fun `pnl calculation should be correct for loss-making holding`() {
        // Given
        val holding = Holding(
            symbol = "INFY",
            quantity = 200,
            ltp = 1500.0,
            avgPrice = 1600.0,
            close = 1550.0
        )

        // When
        val pnl = holding.pnl

        // Then
        val expectedPnl = (1500.0 - 1600.0) * 200 // (LTP - AvgPrice) * Quantity
        assertEquals(expectedPnl, pnl, 0.1)
        assertEquals(-20000.0, pnl, 0.1) // Should be negative (loss-making)
    }

    @Test
    fun `pnl calculation should be zero when ltp equals avgPrice`() {
        // Given
        val holding = Holding(
            symbol = "TCS",
            quantity = 50,
            ltp = 3800.0,
            avgPrice = 3800.0,
            close = 3750.0
        )

        // When
        val pnl = holding.pnl

        // Then
        assertEquals(0.0, pnl, 0.1)
    }

    @Test
    fun `todaysPnl calculation should be correct for positive daily change`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val todaysPnl = holding.todaysPnl

        // Then
        val expectedTodaysPnl = (2450.0 - 2500.0) * 100 // (Close - LTP) * Quantity
        assertEquals(expectedTodaysPnl, todaysPnl, 0.1)
        assertEquals(-5000.0, todaysPnl, 0.1) // Should be negative (LTP > Close)
    }

    @Test
    fun `todaysPnl calculation should be correct for negative daily change`() {
        // Given
        val holding = Holding(
            symbol = "TCS",
            quantity = 50,
            ltp = 3800.0,
            avgPrice = 3700.0,
            close = 3850.0
        )

        // When
        val todaysPnl = holding.todaysPnl

        // Then
        val expectedTodaysPnl = (3850.0 - 3800.0) * 50 // (Close - LTP) * Quantity
        assertEquals(expectedTodaysPnl, todaysPnl, 0.1)
        assertEquals(2500.0, todaysPnl, 0.1) // Should be positive (Close > LTP)
    }

    @Test
    fun `todaysPnl calculation should be zero when close equals ltp`() {
        // Given
        val holding = Holding(
            symbol = "WIPRO",
            quantity = 150,
            ltp = 400.0,
            avgPrice = 450.0,
            close = 400.0
        )

        // When
        val todaysPnl = holding.todaysPnl

        // Then
        assertEquals(0.0, todaysPnl, 0.1)
    }

    @Test
    fun `currentValue calculation should be correct`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val currentValue = holding.currentValue

        // Then
        val expectedCurrentValue = 2500.0 * 100 // LTP * Quantity
        assertEquals(expectedCurrentValue, currentValue, 0.1)
        assertEquals(250000.0, currentValue, 0.1)
    }

    @Test
    fun `currentValue calculation should be zero when quantity is zero`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 0,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val currentValue = holding.currentValue

        // Then
        assertEquals(0.0, currentValue, 0.1)
    }

    @Test
    fun `totalInvestment calculation should be correct`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val totalInvestment = holding.totalInvestment

        // Then
        val expectedTotalInvestment = 2400.0 * 100 // AvgPrice * Quantity
        assertEquals(expectedTotalInvestment, totalInvestment, 0.1)
        assertEquals(240000.0, totalInvestment, 0.1)
    }

    @Test
    fun `totalInvestment calculation should be zero when quantity is zero`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 0,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val totalInvestment = holding.totalInvestment

        // Then
        assertEquals(0.0, totalInvestment, 0.1)
    }

    @Test
    fun `holding properties should be correctly set`() {
        // Given
        val symbol = "RELIANCE"
        val quantity = 100
        val ltp = 2500.0
        val avgPrice = 2400.0
        val close = 2450.0

        // When
        val holding = Holding(
            symbol = symbol,
            quantity = quantity,
            ltp = ltp,
            avgPrice = avgPrice,
            close = close
        )

        // Then
        assertEquals(symbol, holding.symbol)
        assertEquals(quantity, holding.quantity)
        assertEquals(ltp, holding.ltp, 0.1)
        assertEquals(avgPrice, holding.avgPrice, 0.1)
        assertEquals(close, holding.close, 0.1)
    }

    @Test
    fun `holding with decimal values should calculate correctly`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 75,
            ltp = 1234.56,
            avgPrice = 1200.00,
            close = 1250.25
        )

        // When
        val pnl = holding.pnl
        val todaysPnl = holding.todaysPnl
        val currentValue = holding.currentValue
        val totalInvestment = holding.totalInvestment

        // Then - Using larger delta to account for floating-point precision
        assertEquals(2592.0, pnl, 0.1) // (1234.56 - 1200.00) * 75 = 34.56 * 75 = 2592.0
        assertEquals(1176.75, todaysPnl, 0.1) // (1250.25 - 1234.56) * 75
        assertEquals(92592.0, currentValue, 0.1) // 1234.56 * 75
        assertEquals(90000.0, totalInvestment, 0.1) // 1200.00 * 75
    }

    @Test
    fun `holding with large numbers should calculate correctly`() {
        // Given
        val holding = Holding(
            symbol = "TATAMOTORS",
            quantity = 10000,
            ltp = 500.0,
            avgPrice = 450.0,
            close = 510.0
        )

        // When
        val pnl = holding.pnl
        val todaysPnl = holding.todaysPnl
        val currentValue = holding.currentValue
        val totalInvestment = holding.totalInvestment

        // Then
        assertEquals(500000.0, pnl, 0.1) // (500.0 - 450.0) * 10000
        assertEquals(100000.0, todaysPnl, 0.1) // (510.0 - 500.0) * 10000
        assertEquals(5000000.0, currentValue, 0.1) // 500.0 * 10000
        assertEquals(4500000.0, totalInvestment, 0.1) // 450.0 * 10000
    }

    @Test
    fun `holding equality should work correctly`() {
        // Given
        val holding1 = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )
        
        val holding2 = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )
        
        val holding3 = Holding(
            symbol = "TCS",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // Then
        assertEquals(holding1, holding2)
        assertNotEquals(holding1, holding3)
        assertEquals(holding1.hashCode(), holding2.hashCode())
        assertNotEquals(holding1.hashCode(), holding3.hashCode())
    }

    @Test
    fun `holding toString should contain all properties`() {
        // Given
        val holding = Holding(
            symbol = "RELIANCE",
            quantity = 100,
            ltp = 2500.0,
            avgPrice = 2400.0,
            close = 2450.0
        )

        // When
        val toString = holding.toString()

        // Then
        assertTrue(toString.contains("RELIANCE"))
        assertTrue(toString.contains("100"))
        assertTrue(toString.contains("2500.0"))
        assertTrue(toString.contains("2400.0"))
        assertTrue(toString.contains("2450.0"))
    }
}
