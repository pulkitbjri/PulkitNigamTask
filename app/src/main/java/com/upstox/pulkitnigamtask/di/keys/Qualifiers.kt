package com.upstox.pulkitnigamtask.di.keys

import javax.inject.Qualifier

/**
 * Custom qualifiers for dependency injection.
 * These qualifiers help distinguish between different implementations
 * of the same type when multiple implementations exist.
 */

/**
 * Qualifier for API base URL.
 * Used when multiple base URLs are needed (e.g., production vs staging).
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiBaseUrl

/**
 * Qualifier for network timeout configuration.
 * Used when different timeout values are needed for different network calls.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkTimeout

/**
 * Qualifier for logging interceptor level.
 * Used when different logging levels are needed for different environments.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingLevel

/**
 * Qualifier for database name.
 * Used when multiple databases are needed in the application.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName
