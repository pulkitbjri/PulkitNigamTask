package com.upstox.pulkitnigamtask.domain.use_case

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class GetHoldingsUseCaseTest {

    private lateinit var useCase: GetHoldingsUseCase
    private lateinit var mockRepository: HoldingsRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetHoldingsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success with holdings when repository succeeds`() = runTest {
        // Given
        val expectedHoldings = listOf(
            Holding(
                symbol = "RELIANCE",
                quantity = 100,
                ltp = 2500.0,
                avgPrice = 2400.0,
                close = 2450.0
            ),
            Holding(
                symbol = "TCS",
                quantity = 50,
                ltp = 3800.0,
                avgPrice = 3700.0,
                close = 3750.0
            )
        )
        
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(expectedHoldings))

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isSuccess)
        val holdings = result.getOrThrow()
        assertEquals(2, holdings.size)
        assertEquals("RELIANCE", holdings[0].symbol)
        assertEquals("TCS", holdings[1].symbol)
        assertEquals(100, holdings[0].quantity)
        assertEquals(50, holdings[1].quantity)
    }

    @Test
    fun `invoke should return empty list when repository returns empty holdings`() = runTest {
        // Given
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(emptyList()))

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isSuccess)
        val holdings = result.getOrThrow()
        assertTrue(holdings.isEmpty())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.failure(exception))

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke should propagate repository error with custom message`() = runTest {
        // Given
        val customException = RuntimeException("API rate limit exceeded")
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.failure(customException))

        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertEquals("API rate limit exceeded", exception?.message)
        assertTrue(exception is RuntimeException)
    }

    @Test
    fun `invoke should handle multiple repository calls correctly`() = runTest {
        // Given
        val firstCallHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0)
        )
        val secondCallHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0),
            Holding(symbol = "TCS", quantity = 50, ltp = 3800.0, avgPrice = 3700.0, close = 3750.0)
        )
        
        coEvery { mockRepository.getHoldings() } returnsMany listOf(
            flowOf(Result.success(firstCallHoldings)),
            flowOf(Result.success(secondCallHoldings))
        )

        // When - First call
        val firstResult = useCase.invoke().first()
        
        // Then - First call
        assertTrue(firstResult.isSuccess)
        assertEquals(1, firstResult.getOrThrow().size)
        assertEquals("RELIANCE", firstResult.getOrThrow()[0].symbol)

        // When - Second call
        val secondResult = useCase.invoke().first()
        
        // Then - Second call
        assertTrue(secondResult.isSuccess)
        assertEquals(2, secondResult.getOrThrow().size)
        assertEquals("TCS", secondResult.getOrThrow()[1].symbol)
    }
}
