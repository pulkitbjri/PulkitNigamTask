package com.upstox.pulkitnigamtask.util

/**
 * A sealed class representing the result of an operation that can either succeed or fail.
 * Provides type-safe error handling and functional programming patterns.
 */
sealed class Result<out T> {
    
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    /**
     * Maps the success value using the provided function.
     * @param transform Function to transform the success data
     * @return New Result with transformed data
     */
    fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }
    
    /**
     * Maps the error using the provided function.
     * @param transform Function to transform the error
     * @return New Result with transformed error
     */
    fun mapError(transform: (Throwable) -> Throwable): Result<T> {
        return when (this) {
            is Success -> this
            is Error -> Error(transform(exception))
        }
    }
    
    /**
     * Executes the provided function on success, or returns the error.
     * @param transform Function that returns a new Result
     * @return New Result from the transform function
     */
    fun <R> flatMap(transform: (T) -> Result<R>): Result<R> {
        return when (this) {
            is Success -> transform(data)
            is Error -> this
        }
    }
    
    /**
     * Executes the provided function on success, or returns the default value.
     * @param default Default value to return on error
     * @param transform Function to transform the success data
     * @return Transformed data or default value
     */
    fun <R> fold(default: R, transform: (T) -> R): R {
        return when (this) {
            is Success -> transform(data)
            is Error -> default
        }
    }
    
    /**
     * Returns the success value or null if it's an error.
     */
    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            is Error -> null
        }
    }
    
    /**
     * Returns the success value or throws the exception if it's an error.
     */
    fun getOrThrow(): T {
        return when (this) {
            is Success -> data
            is Error -> throw exception
        }
    }
    
    /**
     * Returns true if this is a Success result.
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Returns true if this is an Error result.
     */
    fun isError(): Boolean = this is Error
}
