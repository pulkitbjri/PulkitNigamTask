package com.upstox.pulkitnigamtask.di.modules.data

import com.upstox.pulkitnigamtask.data.remote.ApiService
import com.upstox.pulkitnigamtask.di.keys.LoggingLevel
import com.upstox.pulkitnigamtask.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing networking-related dependencies.
 * Contains all network configuration, HTTP client setup, and API service creation.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides HTTP logging interceptor for debugging network requests.
     * Logs request/response bodies for development purposes.
     */
    @Provides
    @Singleton
    @LoggingLevel
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (Constants.Network.DEBUG_MODE) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * Provides OkHttpClient with configured interceptors and timeouts.
     * Includes logging interceptor and connection timeout settings.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(@LoggingLevel loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.Network.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.Network.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.Network.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides Retrofit instance with base URL and JSON converter.
     * Configured with OkHttpClient and Gson converter for JSON parsing.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.Network.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides ApiService interface implementation.
     * Creates the API service using Retrofit for network communication.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
