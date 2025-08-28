package com.upstox.pulkitnigamtask.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
import com.upstox.pulkitnigamtask.domain.use_case.GetHoldingsUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetHoldingsWithSummaryUseCase
import com.upstox.pulkitnigamtask.domain.use_case.GetPortfolioSummaryUseCase
import com.upstox.pulkitnigamtask.domain.use_case.HoldingsWithSummary
import com.upstox.pulkitnigamtask.presentation.model.HoldingsState
import com.upstox.pulkitnigamtask.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldingsWithSummaryUseCase: GetHoldingsWithSummaryUseCase
) : ViewModel() {

    private val _state = MutableLiveData<HoldingsState>()
    val state: LiveData<HoldingsState> = _state

    private val _isSummaryExpanded = MutableLiveData<Boolean>()
    val isSummaryExpanded: LiveData<Boolean> = _isSummaryExpanded

    init {
        initializeViewModel()
    }

    private fun initializeViewModel() {
        _isSummaryExpanded.value = false
        loadHoldings()
    }

    fun loadHoldings() {
        setLoadingState()
        fetchHoldingsData()
    }

    private fun setLoadingState() {
        _state.value = HoldingsState.Loading
    }

    private fun fetchHoldingsData() {
        viewModelScope.launch {
            getHoldingsWithSummaryUseCase()
                .onEach { result ->
                    handleHoldingsResult(result)
                }
                .catch { exception ->
                    handleError(exception)
                }
                .launchIn(viewModelScope)
        }
    }

    private fun handleHoldingsResult(result: Result<HoldingsWithSummary>) {
        result.fold(
            onSuccess = { holdingsWithSummary ->
                setSuccessState(holdingsWithSummary.holdings, holdingsWithSummary.portfolioSummary)
            },
            onFailure = { exception ->
                handleError(exception)
            }
        )
    }

    private fun setSuccessState(
        holdings: List<Holding>,
        summary: PortfolioSummary
    ) {
        _state.value = HoldingsState.Success(holdings, summary)
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = exception.message ?: Constants.UI.DEFAULT_ERROR_MESSAGE
        _state.value = HoldingsState.Error(errorMessage)
    }

    fun toggleSummaryExpansion() {
        val currentValue = _isSummaryExpanded.value ?: false
        _isSummaryExpanded.value = !currentValue
    }

    fun retry() {
        loadHoldings()
    }


}
