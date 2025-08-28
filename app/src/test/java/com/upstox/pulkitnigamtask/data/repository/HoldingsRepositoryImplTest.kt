package com.upstox.pulkitnigamtask.data.repository

import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.data.remote.dto.HoldingsResponse
import com.upstox.pulkitnigamtask.data.remote.dto.HoldingDto
import com.upstox.pulkitnigamtask.data.remote.dto.DataWrapper
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.data.local.dao.HoldingDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HoldingsRepositoryImplTest {

    private lateinit var repository: HoldingsRepositoryImpl
    private lateinit var mockApiService: ApiService
    private lateinit var mockHoldingDao: HoldingDao

    @Before
    fun setup() {
        mockApiService = mockk()
        mockHoldingDao = mockk()
        repository = HoldingsRepositoryImpl(mockApiService, mockHoldingDao)
    }

    @Test
    fun `when API call succeeds, should return holdings list`() = runBlocking {
        // Given
        val apiResponse = HoldingsResponse(
            data = DataWrapper(
                userHolding = listOf(
                    HoldingDto(
                        symbol = "TEST1",
                        quantity = 10,
                        ltp = 100.0,
                        avgPrice = 50.0,
                        close = 95.0
                    )
                )
            )
        )

        coEvery { mockApiService.getHoldings() } returns apiResponse
        coEvery { mockHoldingDao.insertHoldings(any()) } returns Unit

        // When
        val result = repository.getHoldings().first()

        // Then
        assertTrue(result.isSuccess)
        val holdings = result.getOrThrow()
        assertEquals(1, holdings.size)
        assertEquals("TEST1", holdings[0].symbol)
        assertEquals(10, holdings[0].quantity)
        assertEquals(100.0, holdings[0].ltp, 0.1)
    }

    @Test
    fun `when API call fails, should return error`() = runBlocking {
        // Given
        val exception = Exception("Network error")
        coEvery { mockApiService.getHoldings() } throws exception
        coEvery { mockHoldingDao.getAllHoldingsList() } returns emptyList()

        // When
        val result = repository.getHoldings().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
