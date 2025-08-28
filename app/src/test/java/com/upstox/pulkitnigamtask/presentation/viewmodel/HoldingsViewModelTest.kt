package com.upstox.pulkitnigamtask.presentation.viewmodel

import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.util.NetworkUtils
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HoldingsViewModelTest {

    private lateinit var viewModel: HoldingsViewModel
    private lateinit var mockRepository: HoldingsRepository
    private lateinit var mockNetworkUtils: NetworkUtils

    @Before
    fun setUp() {
        mockRepository = mockk()
        mockNetworkUtils = mockk()

        // Mock network connectivity flow
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns MutableStateFlow(true)

        viewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )
    }

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
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns MutableStateFlow(false)
        
        // Create new ViewModel with disconnected network
        val disconnectedViewModel = HoldingsViewModel(
            mockRepository,
            mockNetworkUtils
        )

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

        // When - Change network status
        networkFlow.value = false

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

        // Initially should be false
        assertFalse(testViewModel.isNetworkStateInitialized())

        // After network state is set, should be true
        // The network flow is already emitting, so state should be initialized
        assertTrue(testViewModel.isNetworkStateInitialized())
    }
}
