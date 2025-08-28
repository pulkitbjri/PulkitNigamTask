# PulkitNigamTask - Portfolio Holdings App

A modern Android application built with MVVM + Clean Architecture, demonstrating best practices for scalable and maintainable codebase.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVVM** pattern:

### Layers:
1. **Presentation Layer (UI)** - Activities, Fragments, ViewModels with XML layouts
2. **Domain Layer (Business Logic)** - Use Cases, Domain Models, Repository Interfaces
3. **Data Layer** - Repository Implementations, API, Room Database, DTOs

## 🛠️ Tech Stack

- **Language**: Kotlin 2.0.21
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt 2.52
- **Networking**: Retrofit 2.12.0 + OkHttp 4.12.0
- **Database**: Room Database for local storage
- **UI**: XML Layouts (No Compose)
- **Async Operations**: Kotlin Coroutines & Flow
- **Testing**: JUnit, MockK, Mockito

## 📱 Features

- **Holdings List**: Display portfolio holdings with real-time data
- **Portfolio Summary**: Expandable/collapsible summary with calculated values
- **Real-time P&L**: Color-coded profit/loss indicators
- **Offline Support**: Room database for offline data access
- **Network Connectivity**: Real-time network monitoring with retry functionality
- **Edge-to-Edge Display**: Immersive UI experience
- **Material Design 3**: Modern UI following Material Design 3 guidelines

## 📊 Portfolio Calculations

The app implements the required calculations:

1. **Current Value** = Σ(LTP × Net Quantity)
2. **Total Investment** = Σ(Average Price × Net Quantity)  
3. **Total P&L** = Current Value - Total Investment
4. **Today's P&L** = Σ((Close - LTP) × Net Quantity)

## 📁 Project Structure

```
app/src/main/java/com/upstox/pulkitnigamtask/
├── di/                           # Dependency Injection
│   ├── modules/                  # Dagger Hilt modules
│   └── keys/                     # Custom qualifiers
├── data/                         # Data Layer
│   ├── remote/                   # API related
│   ├── local/                    # Database related
│   └── repository/               # Repository implementations
├── domain/                       # Domain Layer
│   ├── model/                    # Domain models
│   ├── repository/               # Repository interfaces
│   ├── service/                  # Business services
│   ├── use_case/                 # Use cases
│   └── validator/                # Data validation
├── presentation/                 # Presentation Layer
│   ├── adapter/                  # RecyclerView adapters
│   ├── helper/                   # UI helpers
│   ├── model/                    # UI models
│   └── viewmodel/                # ViewModels
├── MainActivity.kt
└── PulkitNigamTaskApplication.kt
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 36 (Android 14)

### Build & Run
1. Clone the repository
2. Open in Android Studio
3. Build and run on device/emulator

### API Endpoint
The app fetches data from:
```
https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/
```

## 📋 Acceptance Criteria Met

✅ **Latest Kotlin version** (2.0.21)  
✅ **XML layouts** (No Compose)  
✅ **Material Design Guidelines**  
✅ **MVVM Architecture**  
✅ **SOLID Principles**  
✅ **Loosely coupled, testable code**  
✅ **Error handling and offline support**  
✅ **Unit tests with good coverage**  
✅ **Min SDK 24+**  
✅ **Responsive UI for all devices**  

## 🎨 Material Design 3 Implementation

### Key Features
- **Color System**: Complete primary/secondary color palettes with proper contrast ratios
- **Typography**: Material 3 text styles for consistent hierarchy
- **Components**: MaterialCardView, MaterialButton, CircularProgressIndicator, MaterialChip
- **Theme Support**: Light and dark themes with WCAG 2.1 AA compliance

## 🔄 Edge-to-Edge Display

### Implementation
- Transparent system bars in both themes
- Dynamic system insets handling
- Proper content positioning to avoid system bars
- Reusable EdgeToEdgeHelper utility class

## 🌐 Network Connectivity Feature

### Features
- Real-time network connectivity monitoring
- Message bar with retry functionality when offline
- Automatic data refresh when connection restored
- Smart retry that prevents unnecessary network calls

### NetworkUtils Class
```kotlin
class NetworkUtils @Inject constructor(
    private val context: Context
) {
    fun isInternetAvailable(): Boolean
    fun getNetworkConnectivityFlow(): Flow<Boolean>
}
```

## 🗄️ Room Database Implementation

### Features
- Local data storage with Room database
- Offline support with cached data
- Reactive updates using Kotlin Flow
- CRUD operations and custom queries
- Integration with Clean Architecture

### Usage Example
```kotlin
@Inject
lateinit var repository: HoldingsRepository

// Get holdings with offline fallback
val holdings = repository.getHoldings()

// Save holdings to database
repository.saveHoldings(holdingsList)
```

## 🔧 Dependency Injection

### Module Structure
- **AppModule**: Main coordinator module
- **NetworkModule**: Networking dependencies
- **RepositoryModule**: Repository implementations
- **UseCaseModule**: Business logic use cases
- **DatabaseModule**: Database dependencies

### Usage Example
```kotlin
@Inject
lateinit var holdingsRepository: HoldingsRepository

@Inject
lateinit var getHoldingsUseCase: GetHoldingsUseCase
```

## 🎨 UI Features

- **Material Design 3**: Modern, accessible design
- **Color-coded P&L**: Green for profit, red for loss
- **Expandable Summary**: Tap to expand/collapse portfolio details
- **Loading States**: Progress indicators during data fetch
- **Error Handling**: User-friendly error messages with retry option
- **Responsive Layout**: Works on all screen sizes

## 🔧 Key Components

### MainActivity
- MVVM implementation with ViewBinding
- LiveData observation for reactive UI updates
- Error handling and retry functionality
- Edge-to-edge display setup

### HoldingsViewModel
- Manages UI state and business logic
- Handles data loading and error states
- Network connectivity state management
- Database operations coordination

### Repository Pattern
- Abstraction layer for data operations
- Supports multiple data sources (API + Database)
- Offline-first approach with automatic fallback

## 📈 Performance Optimizations

- **ViewBinding**: Type-safe view access
- **DiffUtil**: Efficient list updates
- **Coroutines**: Non-blocking async operations
- **RecyclerView**: Memory-efficient list rendering
- **Dagger Hilt**: Efficient dependency injection
- **Room Database**: Optimized local data storage

## 🔒 Error Handling

- Network error handling with retry functionality
- API error responses with user-friendly messages
- Offline state management with cached data
- Database operation error handling
- Graceful degradation when services are unavailable

## 🧪 Testing

### Testing Strategy
- **Unit Tests**: Business logic in Use Cases and Repository
- **Integration Tests**: Repository with API and database
- **Test Frameworks**: JUnit, MockK, Mockito
- **Database Testing**: In-memory database for testing

## 📱 Permissions

Required permissions in AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🚀 Future Enhancements

### Potential Improvements
1. **Database Migrations**: Proper migration strategies
2. **Analytics Integration**: User behavior tracking
3. **Push Notifications**: Real-time portfolio updates
4. **Widgets**: Home screen widgets
5. **Dynamic Color**: Android 12+ dynamic color theming
6. **Motion Patterns**: Material 3 motion patterns

This project demonstrates a production-ready Android application following modern development practices, architectural patterns, and Material Design 3 guidelines.
