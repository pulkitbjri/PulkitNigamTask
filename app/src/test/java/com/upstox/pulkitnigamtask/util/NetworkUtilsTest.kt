package com.upstox.pulkitnigamtask.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NetworkUtilsTest {

    private lateinit var networkUtils: NetworkUtils
    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetwork: Network
    private lateinit var mockNetworkCapabilities: NetworkCapabilities

    @Before
    fun setUp() {
        mockContext = mockk()
        mockConnectivityManager = mockk()
        mockNetwork = mockk()
        mockNetworkCapabilities = mockk()

        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager

        networkUtils = NetworkUtils(mockContext)
    }

    @Test
    fun `isInternetAvailable should return true when network is available and validated`() {
        // Given
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns true

        // When
        val result = networkUtils.isInternetAvailable()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isInternetAvailable should return false when no active network`() {
        // Given
        every { mockConnectivityManager.activeNetwork } returns null

        // When
        val result = networkUtils.isInternetAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isInternetAvailable should return false when network capabilities are null`() {
        // Given
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns null

        // When
        val result = networkUtils.isInternetAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isInternetAvailable should return false when network has no internet capability`() {
        // Given
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns true

        // When
        val result = networkUtils.isInternetAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isInternetAvailable should return false when network is not validated`() {
        // Given
        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns false

        // When
        val result = networkUtils.isInternetAvailable()

        // Then
        assertFalse(result)
    }
}
