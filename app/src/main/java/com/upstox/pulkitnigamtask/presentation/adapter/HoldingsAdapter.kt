package com.upstox.pulkitnigamtask.presentation.adapter

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Holding>?) {
        android.util.Log.d("HoldingsAdapter", "submitList called with ${list?.size ?: 0} items")
        super.submitList(list)
    }

    class HoldingViewHolder(
        private val binding: ItemHoldingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(holding: Holding) {
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
            val areSame = oldItem.symbol == newItem.symbol
            android.util.Log.d("HoldingsAdapter", "areItemsTheSame: $areSame (${oldItem.symbol} vs ${newItem.symbol})")
            return areSame
        }

        override fun areContentsTheSame(oldItem: Holding, newItem: Holding): Boolean {
            val areSame = oldItem == newItem
            android.util.Log.d("HoldingsAdapter", "areContentsTheSame: $areSame (${oldItem.symbol})")
            return areSame
        }
    }
}
