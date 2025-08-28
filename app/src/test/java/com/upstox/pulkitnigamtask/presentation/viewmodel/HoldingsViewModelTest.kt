package com.upstox.pulkitnigamtask.presentation.viewmodel

import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.domain.use_case.GetLocalHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetProfitableHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetLossMakingHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.SaveHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.ClearLocalHoldingsUseCase
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
    private lateinit var mockGetLocalHoldingsUseCase: GetLocalHoldingsUseCase
    private lateinit var mockGetProfitableHoldingsUseCase: GetProfitableHoldingsUseCase
    private lateinit var mockGetLossMakingHoldingsUseCase: GetLossMakingHoldingsUseCase
    private lateinit var mockSaveHoldingsUseCase: SaveHoldingsUseCase
    private lateinit var mockClearLocalHoldingsUseCase: ClearLocalHoldingsUseCase
    private lateinit var mockNetworkUtils: NetworkUtils

    @Before
    fun setUp() {
        mockRepository = mockk()
        mockGetLocalHoldingsUseCase = mockk()
        mockGetProfitableHoldingsUseCase = mockk()
        mockGetLossMakingHoldingsUseCase = mockk()
        mockSaveHoldingsUseCase = mockk()
        mockClearLocalHoldingsUseCase = mockk()
        mockNetworkUtils = mockk()

        // Mock network connectivity flow
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns MutableStateFlow(true)

        viewModel = HoldingsViewModel(
            mockRepository,
            mockGetLocalHoldingsUseCase,
            mockGetProfitableHoldingsUseCase,
            mockGetLossMakingHoldingsUseCase,
            mockSaveHoldingsUseCase,
            mockClearLocalHoldingsUseCase,
            mockNetworkUtils
        )
    }

    @Test
    fun `refreshDataIfConnected should return true and refresh data when network is connected`() = runTest {
        // Given
        every { mockGetLocalHoldingsUseCase() } returns MutableStateFlow(emptyList())
        every { mockGetProfitableHoldingsUseCase() } returns MutableStateFlow(emptyList())
        every { mockGetLossMakingHoldingsUseCase() } returns MutableStateFlow(emptyList())

        // When
        val result = viewModel.refreshDataIfConnected()

        // Then
        assertTrue(result)
        verify { 
            mockGetLocalHoldingsUseCase()
            mockGetProfitableHoldingsUseCase()
            mockGetLossMakingHoldingsUseCase()
        }
    }

    @Test
    fun `refreshDataIfConnected should return false when network is not connected`() = runTest {
        // Given - Set network to disconnected
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns MutableStateFlow(false)
        
        // Create new ViewModel with disconnected network
        val disconnectedViewModel = HoldingsViewModel(
            mockRepository,
            mockGetLocalHoldingsUseCase,
            mockGetProfitableHoldingsUseCase,
            mockGetLossMakingHoldingsUseCase,
            mockSaveHoldingsUseCase,
            mockClearLocalHoldingsUseCase,
            mockNetworkUtils
        )

        // When
        val result = disconnectedViewModel.refreshDataIfConnected()

        // Then
        assertFalse(result)
        verify(exactly = 0) { 
            mockGetLocalHoldingsUseCase()
            mockGetProfitableHoldingsUseCase()
            mockGetLossMakingHoldingsUseCase()
        }
    }

    @Test
    fun `isNetworkConnected should emit network status changes`() = runTest {
        // Given
        val networkFlow = MutableStateFlow(true)
        every { mockNetworkUtils.getNetworkConnectivityFlow() } returns networkFlow

        val testViewModel = HoldingsViewModel(
            mockRepository,
            mockGetLocalHoldingsUseCase,
            mockGetProfitableHoldingsUseCase,
            mockGetLossMakingHoldingsUseCase,
            mockSaveHoldingsUseCase,
            mockClearLocalHoldingsUseCase,
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
            mockGetLocalHoldingsUseCase,
            mockGetProfitableHoldingsUseCase,
            mockGetLossMakingHoldingsUseCase,
            mockSaveHoldingsUseCase,
            mockClearLocalHoldingsUseCase,
            mockNetworkUtils
        )

        // Initially should be false
        assertFalse(testViewModel.isNetworkStateInitialized())

        // After network state is set, should be true
        // The network flow is already emitting, so state should be initialized
        assertTrue(testViewModel.isNetworkStateInitialized())
    }
}
