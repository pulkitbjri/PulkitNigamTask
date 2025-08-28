package com.upstox.pulkitnigamtask.presentation.viewmodel

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
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                repository.getHoldings().collect { result ->
                    result.fold(
                        onSuccess = { holdings ->
                            _allHoldings.value = holdings
                            _isLoading.value = false
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "Unknown error occurred"
                            _isLoading.value = false
                        }
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load holdings"
                _isLoading.value = false
            }
        }
    }



    /**
     * Refresh all data (remote).
     */
    fun refreshData() {
        loadHoldings()
    }

    /**
     * Refresh data only if network is connected.
     * @return true if refresh was initiated, false if no network
     */
    fun refreshDataIfConnected(): Boolean {
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
                _isNetworkConnected.value = isConnected
                // Mark that initial network state has been set
                if (isInitialNetworkState) {
                    isInitialNetworkState = false
                }
            }
        }
    }
}
