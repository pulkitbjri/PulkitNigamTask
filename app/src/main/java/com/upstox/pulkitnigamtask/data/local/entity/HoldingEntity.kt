package com.upstox.pulkitnigamtask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a stock holding in the database.
 * Maps to the Holding domain model.
 */
@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)
