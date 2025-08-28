package com.upstox.pulkitnigamtask.util

/**
 * Application constants used throughout the codebase.
 * Centralizes all constant values for better maintainability.
 */
object Constants {
    
    object Network {
        const val BASE_URL = "https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/"
        const val TIMEOUT_SECONDS = 30L
        const val DEBUG_MODE = true // Set to false for production
    }
    
    object UI {
        const val CURRENCY_SYMBOL = "₹"
        const val DECIMAL_PLACES = 2
        const val DEFAULT_ERROR_MESSAGE = "An unexpected error occurred"
    }
    
    object Validation {
        const val MIN_QUANTITY = 0
        const val MIN_PRICE = 0.0
    }
    
    object Animation {
        const val EXPANSION_DURATION = 300L
    }
}
