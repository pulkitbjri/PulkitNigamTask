package com.upstox.pulkitnigamtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.repository.HoldingsRepository
import com.upstox.pulkitnigamtask.domain.use_case.GetLocalHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetProfitableHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetLossMakingHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.SaveHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.ClearLocalHoldingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing holdings data with Room database integration.
 * Demonstrates how to use local database operations alongside remote data.
 */
@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val repository: HoldingsRepository,
    private val getLocalHoldingsUseCase: GetLocalHoldingsUseCase,
    private val getProfitableHoldingsUseCase: GetProfitableHoldingsUseCase,
    private val getLossMakingHoldingsUseCase: GetLossMakingHoldingsUseCase,
    private val saveHoldingsUseCase: SaveHoldingsUseCase,
    private val clearLocalHoldingsUseCase: ClearLocalHoldingsUseCase
) : ViewModel() {

    // State flows for different data streams
    private val _allHoldings = MutableStateFlow<List<Holding>>(emptyList())
    val allHoldings: StateFlow<List<Holding>> = _allHoldings.asStateFlow()

    private val _localHoldings = MutableStateFlow<List<Holding>>(emptyList())
    val localHoldings: StateFlow<List<Holding>> = _localHoldings.asStateFlow()

    private val _profitableHoldings = MutableStateFlow<List<Holding>>(emptyList())
    val profitableHoldings: StateFlow<List<Holding>> = _profitableHoldings.asStateFlow()

    private val _lossMakingHoldings = MutableStateFlow<List<Holding>>(emptyList())
    val lossMakingHoldings: StateFlow<List<Holding>> = _lossMakingHoldings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Load local holdings on initialization
        loadLocalHoldings()
        loadProfitableHoldings()
        loadLossMakingHoldings()
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
     * Load holdings from local database only.
     */
    fun loadLocalHoldings() {
        viewModelScope.launch {
            try {
                getLocalHoldingsUseCase().collect { holdings ->
                    _localHoldings.value = holdings
                }
            } catch (e: Exception) {
                _error.value = "Failed to load local holdings: ${e.message}"
            }
        }
    }

    /**
     * Load profitable holdings from local database.
     */
    fun loadProfitableHoldings() {
        viewModelScope.launch {
            try {
                getProfitableHoldingsUseCase().collect { holdings ->
                    _profitableHoldings.value = holdings
                }
            } catch (e: Exception) {
                _error.value = "Failed to load profitable holdings: ${e.message}"
            }
        }
    }

    /**
     * Load loss-making holdings from local database.
     */
    fun loadLossMakingHoldings() {
        viewModelScope.launch {
            try {
                getLossMakingHoldingsUseCase().collect { holdings ->
                    _lossMakingHoldings.value = holdings
                }
            } catch (e: Exception) {
                _error.value = "Failed to load loss-making holdings: ${e.message}"
            }
        }
    }

    /**
     * Save holdings to local database.
     */
    fun saveHoldings(holdings: List<Holding>) {
        viewModelScope.launch {
            try {
                saveHoldingsUseCase(holdings)
                // Reload local data after saving
                loadLocalHoldings()
                loadProfitableHoldings()
                loadLossMakingHoldings()
            } catch (e: Exception) {
                _error.value = "Failed to save holdings: ${e.message}"
            }
        }
    }

    /**
     * Clear all holdings from local database.
     */
    fun clearLocalHoldings() {
        viewModelScope.launch {
            try {
                clearLocalHoldingsUseCase()
                // Clear all local data
                _localHoldings.value = emptyList()
                _profitableHoldings.value = emptyList()
                _lossMakingHoldings.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to clear holdings: ${e.message}"
            }
        }
    }

    /**
     * Clear error state.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Refresh all data (remote + local).
     */
    fun refreshData() {
        loadHoldings()
        loadLocalHoldings()
        loadProfitableHoldings()
        loadLossMakingHoldings()
    }
}
