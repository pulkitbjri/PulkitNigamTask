# Network Connectivity Message Bar Feature

## Overview

This feature adds a message bar on top of the RecyclerView that appears when there's no internet connection and provides a retry option to fetch holdings when connected to the internet.

## Features

### 1. Network Connectivity Detection
- **Real-time monitoring**: Continuously monitors network connectivity status
- **Automatic detection**: Detects when internet connection is lost or restored
- **Background monitoring**: Uses Android's ConnectivityManager to monitor network changes

### 2. Message Bar UI
- **Position**: Located above the RecyclerView
- **Visibility**: Only shows when internet is disconnected
- **Design**: Material Design 3 card with error styling
- **Content**: 
  - Error icon
  - "No internet connection" message
  - "Retry to fetch holdings" button

### 3. User Interaction
- **Retry button**: Allows users to manually retry fetching holdings (only works when internet is connected)
- **Automatic refresh**: Automatically attempts to fetch data when internet is restored
- **User feedback**: Shows snackbar messages for connection status changes (only when status changes, not on app startup)
- **Smart retry**: Prevents unnecessary network calls when offline

## Implementation Details

### 1. NetworkUtils Class
```kotlin
class NetworkUtils @Inject constructor(
    private val context: Context
) {
    fun isInternetAvailable(): Boolean
    fun getNetworkConnectivityFlow(): Flow<Boolean>
}
```

**Key Features:**
- Uses Android's ConnectivityManager API
- Provides both synchronous and reactive network status
- Handles network capability validation
- Uses Kotlin Flow for reactive programming

### 2. ViewModel Integration
```kotlin
class HoldingsViewModel @Inject constructor(
    // ... other dependencies
    private val networkUtils: NetworkUtils
) {
    private val _isNetworkConnected = MutableStateFlow(true)
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected.asStateFlow()
    
    private fun observeNetworkConnectivity()
    fun refreshDataIfConnected(): Boolean
}
```

**Key Features:**
- Observes network connectivity changes
- Updates UI state based on network status
- Integrates with existing data loading logic
- Provides smart retry functionality that checks network status
- Tracks initial network state to avoid unnecessary notifications on app startup

### 3. UI Implementation
```xml
<!-- Network Connectivity Message Bar -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/networkMessageBar"
    android:visibility="gone"
    app:cardBackgroundColor="?attr/colorErrorContainer"
    app:strokeColor="?attr/colorError">
    
    <!-- Message content with retry button -->
</com.google.android.material.card.MaterialCardView>
```

**Key Features:**
- Material Design 3 styling
- Error-themed colors
- Responsive layout
- Accessibility support

### 4. MainActivity Integration
```kotlin
class MainActivity : AppCompatActivity() {
    private fun observeNetworkConnectivity()
    private fun updateNetworkMessageBar(isConnected: Boolean)
    private fun setupClickListeners()
}
```

**Key Features:**
- Observes ViewModel network state
- Updates message bar visibility
- Handles retry button clicks
- Shows user feedback messages

## User Experience

### When Internet is Available:
- Message bar is hidden
- RecyclerView displays normally
- Data loads automatically

### When Internet is Lost:
- Message bar appears with error styling
- Shows "No internet connection" message
- Provides "Retry to fetch holdings" button
- User can manually retry (but will show error if still offline)

### When Internet is Restored:
- Message bar disappears
- Shows "Internet connected" snackbar (only if previously disconnected)
- Automatically attempts to fetch fresh data

## Technical Benefits

### 1. Reactive Architecture
- Uses Kotlin Flow for reactive network monitoring
- Integrates seamlessly with existing MVVM architecture
- Provides real-time network status updates

### 2. Material Design 3
- Follows Material Design 3 guidelines
- Uses semantic colors for error states
- Provides consistent user experience

### 3. Accessibility
- Proper content descriptions
- Keyboard navigation support
- Screen reader compatibility

### 4. Performance
- Efficient network monitoring
- Minimal resource usage
- Background operation support

## Testing

### Unit Tests
- `NetworkUtilsTest`: Tests network connectivity detection
- Covers various network states and edge cases
- Uses MockK for dependency mocking

### Integration Tests
- Network state changes in ViewModel
- UI updates based on network status
- Retry functionality verification

## Dependencies

### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Dagger Hilt Integration
```kotlin
@Provides
@Singleton
fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
    return NetworkUtils(context)
}
```

## Future Enhancements

### Potential Improvements:
1. **Offline caching**: Cache data when internet is available
2. **Smart retry**: Exponential backoff for retry attempts
3. **Network type detection**: Different behavior for WiFi vs mobile data
4. **Custom retry intervals**: User-configurable retry timing
5. **Network quality monitoring**: Monitor connection quality, not just availability

## Usage Example

```kotlin
// In ViewModel
viewModel.isNetworkConnected.collectLatest { isConnected ->
    updateNetworkMessageBar(isConnected)
}

// In Activity - Smart retry with network check
binding.btnNetworkRetry.setOnClickListener {
    if (viewModel.refreshDataIfConnected()) {
        showSnackbar("Retrying to fetch holdings...")
    } else {
        showSnackbar("No internet connection available", isError = true)
    }
}
```

This feature provides a seamless user experience by keeping users informed about network status and providing easy access to retry functionality when needed.
