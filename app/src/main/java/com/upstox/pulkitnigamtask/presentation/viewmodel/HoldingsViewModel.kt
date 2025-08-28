package com.upstox.pulkitnigamtask.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository

import com.upstox.pulkitnigamtask.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing holdings data with Room database integration.
 * Demonstrates how to use local database operations alongside remote data.
 */
@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val repository: HoldingsRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    companion object {
        private const val TAG = "HoldingsViewModel"
    }

    // State flows for different data streams
    private val _allHoldings = MutableStateFlow<List<Holding>>(emptyList())
    val allHoldings: StateFlow<List<Holding>> = _allHoldings.asStateFlow()



    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isNetworkConnected = MutableStateFlow(true)
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected.asStateFlow()

    private var isInitialNetworkState = true

    init {
        // Observe network connectivity
        observeNetworkConnectivity()
    }

    /**
     * Load holdings from remote API with local caching.
     */
    fun loadHoldings() {
        Log.d(TAG, "loadHoldings() called")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d(TAG, "Starting to load holdings...")
            
            try {
                val result = repository.getHoldings().first()
                result.fold(
                    onSuccess = { holdings ->
                        Log.d(TAG, "Holdings loaded successfully: ${holdings.size} items")
                        Log.d(TAG, "Updating _allHoldings StateFlow with ${holdings.size} items")
                        // Clear the StateFlow first to ensure a new emission
                        _allHoldings.value = emptyList()
                        // Then set the new data
                        _allHoldings.value = holdings
                        Log.d(TAG, "_allHoldings StateFlow updated, current value: ${_allHoldings.value.size} items")
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Failed to load holdings", exception)
                        _error.value = exception.message ?: "Unknown error occurred"
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadHoldings", e)
                _error.value = e.message ?: "Failed to load holdings"
                _isLoading.value = false
            }
        }
    }

    /**
     * Test function to manually trigger API call and see the response.
     */
    fun testApiCall() {
        Log.d(TAG, "testApiCall() called")
        viewModelScope.launch {
            try {
                Log.d(TAG, "Testing API call...")
                val result = repository.getHoldings().first()
                Log.d(TAG, "API call result: $result")
                result.fold(
                    onSuccess = { holdings ->
                        Log.d(TAG, "Test API call successful: ${holdings.size} holdings")
                        Log.d(TAG, "First holding: ${holdings.firstOrNull()}")
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Test API call failed", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Test API call exception", e)
            }
        }
    }



    /**
     * Refresh all data (remote).
     */
    fun refreshData() {
        Log.d(TAG, "refreshData() called")
        loadHoldings()
    }

    /**
     * Refresh data only if network is connected.
     * @return true if refresh was initiated, false if no network
     */
    fun refreshDataIfConnected(): Boolean {
        Log.d(TAG, "refreshDataIfConnected() called, network connected: ${_isNetworkConnected.value}")
        return if (_isNetworkConnected.value) {
            refreshData()
            true
        } else {
            false
        }
    }

    /**
     * Check if the initial network state has been set.
     * @return true if initial network state has been determined
     */
    fun isNetworkStateInitialized(): Boolean {
        return !isInitialNetworkState
    }

    /**
     * Observe network connectivity changes.
     */
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkUtils.getNetworkConnectivityFlow().collectLatest { isConnected ->
                Log.d(TAG, "Network connectivity changed: $isConnected")
                _isNetworkConnected.value = isConnected
                // Mark that initial network state has been set
                if (isInitialNetworkState) {
                    isInitialNetworkState = false
                }
            }
        }
    }
}
