package com.upstox.pulkitnigamtask

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.upstox.pulkitnigamtask.databinding.ActivityMainBinding
import com.upstox.pulkitnigamtask.presentation.adapter.HoldingsAdapter
import com.upstox.pulkitnigamtask.presentation.helper.PortfolioUIHelper

import com.upstox.pulkitnigamtask.presentation.viewmodel.HoldingsViewModel
import com.upstox.pulkitnigamtask.util.EdgeToEdgeHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HoldingsViewModel by viewModels()
    private val holdingsAdapter = HoldingsAdapter()
    private lateinit var portfolioUIHelper: PortfolioUIHelper
    private var isSummaryExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUI()
        setupObservers()
        setupClickListeners()
        loadData()
        
        // Handle back press with OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back press logic can be added here
                finish()
            }
        })
    }

    private fun initializeUI() {
        setupViewBinding()
        setupEdgeToEdge()
        setupRecyclerView()
        setupToolbar()
        setupUIHelper()
    }

    private fun setupEdgeToEdge() {
        EdgeToEdgeHelper.setupEdgeToEdge(
            activity = this,
            rootView = binding.root,
            appBarLayout = binding.appBarLayout,
            contentContainer = binding.mainContentContainer
        )
    }

    private fun setupViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerView() {
        binding.rvHoldings.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = holdingsAdapter
            // Add Material 3 scroll behavior
            setHasFixedSize(true)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.portfolio_title)
        }
    }

    private fun setupUIHelper() {
        portfolioUIHelper = PortfolioUIHelper(this)
    }

    private fun loadData() {
        viewModel.loadHoldings()
    }

    private fun setupObservers() {
        observeHoldingsData()
        observeLoadingState()
        observeErrorState()
    }

    private fun observeHoldingsData() {
        lifecycleScope.launch {
            viewModel.allHoldings.collectLatest { holdings ->
                if (holdings.isNotEmpty()) {
                    showSuccessState(holdings)
                }
            }
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                if (isLoading) {
                    showLoadingState()
                }
            }
        }
    }

    private fun observeErrorState() {
        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMessage ->
                errorMessage?.let { showErrorState(it) }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener { 
            viewModel.refreshData()
            showSnackbar("Refreshing portfolio data...")
        }
        binding.layoutPortfolioSummary.setOnClickListener { 
            toggleSummaryExpansion()
        }
    }

    private fun showLoadingState() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvHoldings.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showSuccessState(holdings: List<com.upstox.pulkitnigamtask.domain.model.Holding>) {
        binding.apply {
            progressBar.visibility = View.GONE
            rvHoldings.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
        }
        
        updateHoldingsList(holdings)
        updatePortfolioSummary(holdings)
    }

    private fun showErrorState(message: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            rvHoldings.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            tvErrorMessage.text = message
        }
        
        showSnackbar(message, isError = true)
    }

    private fun updateHoldingsList(holdings: List<com.upstox.pulkitnigamtask.domain.model.Holding>) {
        holdingsAdapter.submitList(holdings)
    }

    private fun updatePortfolioSummary(holdings: List<com.upstox.pulkitnigamtask.domain.model.Holding>) {
        // Calculate portfolio summary from holdings
        val currentValue: Double = holdings.sumOf { it.currentValue }
        val totalInvestment: Double = holdings.sumOf { it.totalInvestment }
        val totalPnl: Double = currentValue - totalInvestment
        val todaysPnl: Double = holdings.sumOf { it.todaysPnl }
        
        binding.apply {
            tvCurrentValue.text = "₹${String.format("%.2f", currentValue)}"
            tvTotalInvestment.text = "₹${String.format("%.2f", totalInvestment)}"
            tvTodaysPnl.text = "₹${String.format("%.2f", todaysPnl)}"
            tvTotalPnl.text = "₹${String.format("%.2f", totalPnl)}"
            
            // Set colors based on PnL
            val todaysPnlColor = if (todaysPnl >= 0) R.color.success_60 else R.color.error_60
            val totalPnlColor = if (totalPnl >= 0) R.color.success_60 else R.color.error_60
            
            tvTodaysPnl.setTextColor(ContextCompat.getColor(this@MainActivity, todaysPnlColor))
            tvTotalPnl.setTextColor(ContextCompat.getColor(this@MainActivity, totalPnlColor))
        }
    }

    private fun toggleSummaryExpansion() {
        isSummaryExpanded = !isSummaryExpanded
        updateSummaryExpansion(isSummaryExpanded)
    }

    private fun updateSummaryExpansion(isExpanded: Boolean) {
        binding.apply {
            layoutSummaryDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
            ivExpandArrow.setImageResource(
                if (isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
            )
        }
    }

    private fun showSnackbar(message: String, isError: Boolean = false) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        
        if (isError) {
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.error_50))
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.refreshData()
                showSnackbar("Refreshing portfolio data...")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}