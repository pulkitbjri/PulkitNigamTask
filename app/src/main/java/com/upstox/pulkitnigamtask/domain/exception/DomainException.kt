package com.upstox.pulkitnigamtask.domain.exception

/**
 * Base exception for domain layer errors.
 * This provides a common base for all domain-specific exceptions.
 */
sealed class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when portfolio data is invalid.
 */
class InvalidPortfolioDataException(message: String, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Exception thrown when holdings data is invalid.
 */
class InvalidHoldingsDataException(message: String, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Exception thrown when portfolio calculations fail.
 */
class PortfolioCalculationException(message: String, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Exception thrown when data validation fails.
 */
class ValidationException(message: String, cause: Throwable? = null) : DomainException(message, cause)
