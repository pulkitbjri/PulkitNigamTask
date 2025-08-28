package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.service.PortfolioCalculationService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPortfolioSummaryUseCaseTest {

    private lateinit var useCase: GetPortfolioSummaryUseCase
    private lateinit var mockPortfolioCalculationService: PortfolioCalculationService

    @Before
    fun setup() {
        mockPortfolioCalculationService = mockk()
        useCase = GetPortfolioSummaryUseCase(mockPortfolioCalculationService)
    }

    @Test
    fun `when holdings provided, should calculate portfolio summary correctly`() = runBlocking {
        // Given
        val holdings = listOf(
            Holding(
                symbol = "TEST1",
                quantity = 10,
                ltp = 100.0,
                avgPrice = 50.0,
                close = 95.0
            ),
            Holding(
                symbol = "TEST2",
                quantity = 5,
                ltp = 200.0,
                avgPrice = 50.0,
                close = 210.0
            )
        )

        val expectedSummary = PortfolioSummary(
            currentValue = 2000.0, // (100 * 10) + (200 * 5)
            totalInvestment = 750.0, // (50 * 10) + (50 * 5)
            totalPnl = 1250.0, // 2000 - 750
            todaysPnl = -75.0 // (95-100)*10 + (210-200)*5
        )

        every { mockPortfolioCalculationService.calculatePortfolioSummary(holdings) } returns expectedSummary

        // When
        val result = useCase(holdings)

        // Then
        assertEquals(expectedSummary, result)
    }

    @Test
    fun `when empty holdings provided, should return zero values`() = runBlocking {
        // Given
        val holdings = emptyList<Holding>()
        val expectedSummary = PortfolioSummary(
            currentValue = 0.0,
            totalInvestment = 0.0,
            totalPnl = 0.0,
            todaysPnl = 0.0
        )

        every { mockPortfolioCalculationService.calculatePortfolioSummary(holdings) } returns expectedSummary

        // When
        val result = useCase(holdings)

        // Then
        assertEquals(expectedSummary, result)
    }
}
