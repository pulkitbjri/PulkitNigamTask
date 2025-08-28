package com.upstox.pulkitnigamtask

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.upstox.pulkitnigamtask.databinding.ActivityMainBinding
import com.upstox.pulkitnigamtask.presentation.adapter.HoldingsAdapter
import com.upstox.pulkitnigamtask.presentation.helper.PortfolioUIHelper
import com.upstox.pulkitnigamtask.presentation.model.HoldingsState
import com.upstox.pulkitnigamtask.presentation.viewmodel.HoldingsViewModel
import com.upstox.pulkitnigamtask.util.EdgeToEdgeHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HoldingsViewModel by viewModels()
    private val holdingsAdapter = HoldingsAdapter()
    private lateinit var portfolioUIHelper: PortfolioUIHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUI()
        setupObservers()
        setupClickListeners()
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

    private fun setupObservers() {
        observeHoldingsState()
        observeSummaryExpansion()
    }

    private fun observeHoldingsState() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is HoldingsState.Loading -> showLoadingState()
                is HoldingsState.Success -> showSuccessState(state.holdings, state.portfolioSummary)
                is HoldingsState.Error -> showErrorState(state.message)
            }
        })
    }

    private fun observeSummaryExpansion() {
        viewModel.isSummaryExpanded.observe(this, Observer { isExpanded ->
            updateSummaryExpansion(isExpanded)
        })
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener { 
            viewModel.retry()
            showSnackbar("Refreshing portfolio data...")
        }
        binding.layoutPortfolioSummary.setOnClickListener { 
            viewModel.toggleSummaryExpansion()
        }
    }

    private fun showLoadingState() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvHoldings.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
    }

    private fun showSuccessState(
        holdings: List<com.upstox.pulkitnigamtask.domain.model.Holding>,
        summary: com.upstox.pulkitnigamtask.domain.model.PortfolioSummary
    ) {
        binding.apply {
            progressBar.visibility = View.GONE
            rvHoldings.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
        }
        
        updateHoldingsList(holdings)
        updatePortfolioSummary(summary)
        
        // Show success feedback
        if (holdings.isNotEmpty()) {
            showSnackbar("Portfolio loaded successfully")
        }
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

    private fun updatePortfolioSummary(summary: com.upstox.pulkitnigamtask.domain.model.PortfolioSummary) {
        val formattedSummary = portfolioUIHelper.formatPortfolioSummary(summary)
        binding.apply {
            tvCurrentValue.text = formattedSummary.currentValue
            tvTotalInvestment.text = formattedSummary.totalInvestment
            tvTodaysPnl.text = formattedSummary.todaysPnl
            tvTotalPnl.text = formattedSummary.totalPnl
            tvTodaysPnl.setTextColor(formattedSummary.todaysPnlColor)
            tvTotalPnl.setTextColor(formattedSummary.totalPnlColor)
        }
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
                viewModel.retry()
                showSnackbar("Refreshing portfolio data...")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}