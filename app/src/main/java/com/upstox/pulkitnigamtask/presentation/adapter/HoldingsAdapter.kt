package com.upstox.pulkitnigamtask.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.upstox.pulkitnigamtask.R
import com.upstox.pulkitnigamtask.databinding.ItemHoldingBinding
import com.upstox.pulkitnigamtask.domain.model.Holding
import com.upstox.pulkitnigamtask.util.FormattingUtils

class HoldingsAdapter : ListAdapter<Holding, HoldingsAdapter.HoldingViewHolder>(HoldingDiffCallback()) {

    companion object {
        private const val TAG = "HoldingsAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        val item = getItem(position)
        Log.d(TAG, "Binding item at position $position: ${item.symbol}")
        holder.bind(item)
    }

    override fun submitList(list: List<Holding>?) {
        Log.d(TAG, "submitList called with ${list?.size ?: 0} items")
        super.submitList(list)
    }

    class HoldingViewHolder(
        private val binding: ItemHoldingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(holding: Holding) {
            Log.d(TAG, "Binding holding: ${holding.symbol}")
            binding.apply {
                setStockInfo(holding)
                setPnlInfo(holding)
            }
        }

        private fun setStockInfo(holding: Holding) {
            binding.apply {
                tvStockSymbol.text = holding.symbol
                tvLtp.text = formatLtp(holding.ltp)
                tvNetQuantity.text = formatQuantity(holding.quantity)
            }
        }

        private fun setPnlInfo(holding: Holding) {
            binding.apply {
                tvPnl.text = formatPnl(holding.pnl)
                tvPnl.setTextColor(getPnlColor(holding.pnl))
            }
        }

        private fun formatLtp(ltp: Double): String = FormattingUtils.formatLtp(ltp)

        private fun formatQuantity(quantity: Int): String = FormattingUtils.formatQuantity(quantity)

        private fun formatPnl(pnl: Double): String = FormattingUtils.formatPnl(pnl)

        private fun getPnlColor(pnl: Double): Int {
            val colorRes = if (pnl >= 0) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            return ContextCompat.getColor(itemView.context, colorRes)
        }
    }

    private class HoldingDiffCallback : DiffUtil.ItemCallback<Holding>() {
        override fun areItemsTheSame(oldItem: Holding, newItem: Holding): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Holding, newItem: Holding): Boolean {
            return oldItem == newItem
        }
    }
}
