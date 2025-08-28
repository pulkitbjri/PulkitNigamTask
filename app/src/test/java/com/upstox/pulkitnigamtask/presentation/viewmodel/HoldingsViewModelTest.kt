package com.upstox.pulkitnigamtask.presentation.viewmodel

import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.util.NetworkUtils

import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HoldingsViewModelTest {

    private lateinit var viewModel: HoldingsViewModel
    private lateinit var mockRepository: HoldingsRepository
    private lateinit var mockNetworkUtils: NetworkUtils
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockRepository = mockk()
        mockNetworkUtils = mockk()
        Dispatchers.setMain(testDispatcher)

        // Mock network connectivity flow
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns MutableStateFlow(true)

        viewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )
        
        // Advance dispatcher to allow init block to complete
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Network Connectivity Tests
    @Test
    fun `refreshDataIfConnected should return true and refresh data when network is connected`() = runTest {
        // Given

        // When
        val result = viewModel.refreshDataIfConnected()

        // Then
        assertTrue(result)
    }

    @Test
    fun `refreshDataIfConnected should return false when network is not connected`() = runTest {
        // Given - Set network to disconnected
        val disconnectedNetworkFlow = MutableStateFlow(false)
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns disconnectedNetworkFlow
        
        // Create new ViewModel with disconnected network
        val disconnectedViewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )
        
        // Advance dispatcher to allow init block to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val result = disconnectedViewModel.refreshDataIfConnected()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isNetworkConnected should emit network status changes`() = runTest {
        // Given
        val networkFlow = MutableStateFlow(true)
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns networkFlow

        val testViewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )
        
        // Advance dispatcher to allow init block to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // When - Change network status
        networkFlow.value = false
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(false, testViewModel.isNetworkConnected.value)
    }

    @Test
    fun `isNetworkStateInitialized should return false initially and true after network state is set`() = runTest {
        // Given
        val networkFlow = MutableStateFlow(true)
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns networkFlow

        val testViewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )

        // Initially should be false (before network state is observed)
        assertFalse(testViewModel.isNetworkStateInitialized())

        // Advance dispatcher to allow init block to complete and network state to be observed
        testDispatcher.scheduler.advanceUntilIdle()

        // After network state is set, should be true
        assertTrue(testViewModel.isNetworkStateInitialized())
    }

    // Data Loading Tests
    @Test
    fun `loadHoldings should update holdings when repository succeeds`() = runTest {
        // Given
        val expectedHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0),
            Holding(symbol = "TCS", quantity = 50, ltp = 3800.0, avgPrice = 3700.0, close = 3750.0)
        )
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(expectedHoldings))

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(expectedHoldings, viewModel.allHoldings.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadHoldings should handle empty holdings list`() = runTest {
        // Given
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(emptyList()))

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allHoldings.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadHoldings should set error when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.failure(exception))

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allHoldings.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertEquals("Network error", viewModel.error.value)
    }

    @Test
    fun `loadHoldings should handle unknown error`() = runTest {
        // Given
        val exception = Exception()
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.failure(exception))

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allHoldings.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertEquals("Unknown error occurred", viewModel.error.value)
    }

    @Test
    fun `loadHoldings should set loading state correctly`() = runTest {
        // Given
        val expectedHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0)
        )
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(expectedHoldings))

        // When
        viewModel.loadHoldings()
        
        // Advance time to complete loading
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - Should not be loading after completion and should have data
        assertFalse(viewModel.isLoading.value)
        assertEquals(expectedHoldings, viewModel.allHoldings.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `refreshData should call loadHoldings`() = runTest {
        // Given
        val expectedHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0)
        )
        coEvery { mockRepository.getHoldings() } returns flowOf(Result.success(expectedHoldings))

        // When
        viewModel.refreshData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(expectedHoldings, viewModel.allHoldings.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadHoldings should clear previous error when starting new load`() = runTest {
        // Given
        val firstException = Exception("First error")
        val secondHoldings = listOf(
            Holding(symbol = "RELIANCE", quantity = 100, ltp = 2500.0, avgPrice = 2400.0, close = 2450.0)
        )
        
        coEvery { mockRepository.getHoldings() } returnsMany listOf(
            flowOf(Result.failure(firstException)),
            flowOf(Result.success(secondHoldings))
        )

        // When - First load fails
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - Should have error
        assertEquals("First error", viewModel.error.value)
        
        // When - Second load succeeds
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - Error should be cleared
        assertNull(viewModel.error.value)
        assertEquals(secondHoldings, viewModel.allHoldings.value)
    }

    @Test
    fun `loadHoldings should handle exception in flow collection`() = runTest {
        // Given
        coEvery { mockRepository.getHoldings() } throws RuntimeException("Flow collection error")

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allHoldings.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertEquals("Flow collection error", viewModel.error.value)
    }

    @Test
    fun `loadHoldings should handle exception with null message`() = runTest {
        // Given
        coEvery { mockRepository.getHoldings() } throws RuntimeException()

        // When
        viewModel.loadHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allHoldings.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertEquals("Failed to load holdings", viewModel.error.value)
    }
}
