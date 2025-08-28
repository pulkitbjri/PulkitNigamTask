package com.upstox.pulkitnigamtask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HoldingsResponse(
    @SerializedName("data")
    val data: DataWrapper
)

data class DataWrapper(
    @SerializedName("userHolding")
    val userHolding: List<HoldingDto>
)

data class HoldingDto(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("ltp")
    val ltp: Double,
    @SerializedName("avgPrice")
    val avgPrice: Double,
    @SerializedName("close")
    val close: Double
)
