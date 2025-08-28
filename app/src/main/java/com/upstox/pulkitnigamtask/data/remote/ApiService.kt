package com.upstox.pulkitnigamtask.data.remote

import com.upstox.pulkitnigamtask.data.remote.dto.HoldingsResponse
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun getHoldings(): HoldingsResponse
}
