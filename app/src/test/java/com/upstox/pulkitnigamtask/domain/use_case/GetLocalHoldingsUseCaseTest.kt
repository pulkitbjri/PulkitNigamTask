package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLocalHoldingsUseCaseTest {

    private lateinit var mockRepository: HoldingsRepository
    private lateinit var getLocalHoldingsUseCase: GetLocalHoldingsUseCase
    private lateinit var getProfitableHoldingsUseCase: GetProfitableHoldingsUseCase
    private lateinit var getLossMakingHoldingsUseCase: GetLossMakingHoldingsUseCase
    private lateinit var saveHoldingsUseCase: SaveHoldingsUseCase
    private lateinit var clearLocalHoldingsUseCase: ClearLocalHoldingsUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()
        getLocalHoldingsUseCase = GetLocalHoldingsUseCase(mockRepository)
        getProfitableHoldingsUseCase = GetProfitableHoldingsUseCase(mockRepository)
        getLossMakingHoldingsUseCase = GetLossMakingHoldingsUseCase(mockRepository)
        saveHoldingsUseCase = SaveHoldingsUseCase(mockRepository)
        clearLocalHoldingsUseCase = ClearLocalHoldingsUseCase(mockRepository)
    }

    // GetLocalHoldingsUseCase Tests
    @Test
    fun `GetLocalHoldingsUseCase invoke should return local holdings`() = runTest {
        // Given
        val expectedHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0),
            Holding(symbol = "TCS", quantity = 50, ltp = 3800.0, avgPrice = 3700.0, close = 3750.0)
        )
        coEvery { mockRepository.getLocalHoldings() } returns flowOf(expectedHoldings)

        // When
        val result = getLocalHoldingsUseCase.invoke().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("RELIANCE", result[0].symbol)
        assertEquals("TCS", result[1].symbol)
    }

    @Test
    fun `GetLocalHoldingsUseCase invoke should return empty list when no local holdings`() = runTest {
        // Given
        coEvery { mockRepository.getLocalHoldings() } returns flowOf(emptyList())

        // When
        val result = getLocalHoldingsUseCase.invoke().first()

        // Then
        assertTrue(result.isEmpty())
    }

    // GetProfitableHoldingsUseCase Tests
    @Test
    fun `GetProfitableHoldingsUseCase invoke should return only profitable holdings`() = runTest {
        // Given
        val profitableHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0), // P&L: +10000
            Holding(symbol = "TCS", quantity = 50, ltp = 3800.0, avgPrice = 3700.0, close = 3750.0) // P&L: +5000
        )
        coEvery { mockRepository.getProfitableHoldings() } returns flowOf(profitableHoldings)

        // When
        val result = getProfitableHoldingsUseCase.invoke().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.pnl > 0 })
        assertEquals("RELIANCE", result[0].symbol)
        assertEquals("TCS", result[1].symbol)
    }

    @Test
    fun `GetProfitableHoldingsUseCase invoke should return empty list when no profitable holdings`() = runTest {
        // Given
        coEvery { mockRepository.getProfitableHoldings() } returns flowOf(emptyList())

        // When
        val result = getProfitableHoldingsUseCase.invoke().first()

        // Then
        assertTrue(result.isEmpty())
    }

    // GetLossMakingHoldingsUseCase Tests
    @Test
    fun `GetLossMakingHoldingsUseCase invoke should return only loss-making holdings`() = runTest {
        // Given
        val lossMakingHoldings = listOf(
            Holding(symbol = "INFY", quantity = 200, ltp = 1500.0, avgPrice = 1600.0, close = 1550.0), // P&L: -20000
            Holding(symbol = "WIPRO", quantity = 150, ltp = 400.0, avgPrice = 450.0, close = 420.0) // P&L: -7500
        )
        coEvery { mockRepository.getLossMakingHoldings() } returns flowOf(lossMakingHoldings)

        // When
        val result = getLossMakingHoldingsUseCase.invoke().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.pnl < 0 })
        assertEquals("INFY", result[0].symbol)
        assertEquals("WIPRO", result[1].symbol)
    }

    @Test
    fun `GetLossMakingHoldingsUseCase invoke should return empty list when no loss-making holdings`() = runTest {
        // Given
        coEvery { mockRepository.getLossMakingHoldings() } returns flowOf(emptyList())

        // When
        val result = getLossMakingHoldingsUseCase.invoke().first()

        // Then
        assertTrue(result.isEmpty())
    }

    // SaveHoldingsUseCase Tests
    @Test
    fun `SaveHoldingsUseCase invoke should save holdings to repository`() = runTest {
        // Given
        val holdingsToSave = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0),
            Holding(symbol = "TCS", quantity = 50, ltp = 3800.0, avgPrice = 3700.0, close = 3750.0)
        )
        coEvery { mockRepository.saveHoldings(holdingsToSave) } returns Unit

        // When
        saveHoldingsUseCase.invoke(holdingsToSave)

        // Then
        coVerify { mockRepository.saveHoldings(holdingsToSave) }
    }

    @Test
    fun `SaveHoldingsUseCase invoke should handle empty holdings list`() = runTest {
        // Given
        val emptyHoldings = emptyList<Holding>()
        coEvery { mockRepository.saveHoldings(emptyHoldings) } returns Unit

        // When
        saveHoldingsUseCase.invoke(emptyHoldings)

        // Then
        coVerify { mockRepository.saveHoldings(emptyHoldings) }
    }

    @Test
    fun `SaveHoldingsUseCase invoke should handle large holdings list`() = runTest {
        // Given
        val largeHoldingsList = (1..100).map { index ->
            Holding(
                symbol = "STOCK$index",
                quantity = index * 10,
                ltp = 100.0 + index,
                avgPrice = 100.0,
                close = 100.0 + index
            )
        }
        coEvery { mockRepository.saveHoldings(largeHoldingsList) } returns Unit

        // When
        saveHoldingsUseCase.invoke(largeHoldingsList)

        // Then
        coVerify { mockRepository.saveHoldings(largeHoldingsList) }
    }

    // ClearLocalHoldingsUseCase Tests
    @Test
    fun `ClearLocalHoldingsUseCase invoke should clear all local holdings`() = runTest {
        // Given
        coEvery { mockRepository.clearLocalHoldings() } returns Unit

        // When
        clearLocalHoldingsUseCase.invoke()

        // Then
        coVerify { mockRepository.clearLocalHoldings() }
    }

    @Test
    fun `ClearLocalHoldingsUseCase invoke should handle multiple clear operations`() = runTest {
        // Given
        coEvery { mockRepository.clearLocalHoldings() } returns Unit

        // When
        clearLocalHoldingsUseCase.invoke()
        clearLocalHoldingsUseCase.invoke()

        // Then
        coVerify(exactly = 2) { mockRepository.clearLocalHoldings() }
    }
}
