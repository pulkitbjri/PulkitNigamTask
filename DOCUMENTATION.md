# PulkitNigamTask - Portfolio Holdings App

A modern Android application built with MVVM + Clean Architecture, demonstrating best practices for scalable and maintainable codebase.

## 🏗️ Architecture

**Clean Architecture + MVVM Pattern:**
- **Presentation Layer**: Activities, Fragments, ViewModels with XML layouts
- **Domain Layer**: Use Cases, Domain Models, Repository Interfaces
- **Data Layer**: Repository Implementations, API, Room Database

## 🛠️ Tech Stack

- **Language**: Kotlin 2.0.21
- **Architecture**: MVVM + Clean Architecture
- **DI**: Dagger Hilt 2.52
- **Networking**: Retrofit 2.12.0 + OkHttp 4.12.0
- **Database**: Room Database
- **UI**: XML Layouts (Material Design 3)
- **Async**: Kotlin Coroutines & Flow
- **Testing**: JUnit, MockK, Mockito

## 📱 Features

- **Portfolio Holdings**: Real-time holdings display with P&L calculations
- **Offline Support**: Room database with network fallback
- **Material Design 3**: Modern UI with edge-to-edge display
- **Network Monitoring**: Real-time connectivity with retry functionality
- **Responsive Design**: Works on all screen sizes

## 📊 Portfolio Calculations

1. **Current Value** = Σ(LTP × Net Quantity)
2. **Total Investment** = Σ(Average Price × Net Quantity)  
3. **Total P&L** = Current Value - Total Investment
4. **Today's P&L** = Σ((Close - LTP) × Net Quantity)

## 📁 Project Structure

```
app/src/main/java/com/upstox/pulkitnigamtask/
├── di/                           # Dependency Injection
│   ├── modules/
│   │   ├── AppModule.kt
│   │   ├── data/
│   │   │   ├── NetworkModule.kt
│   │   │   ├── RepositoryModule.kt
│   │   │   └── DatabaseModule.kt
│   │   └── domain/
│   │       └── UseCaseModule.kt
│   └── keys/
│       └── Qualifiers.kt
├── data/                         # Data Layer
│   ├── remote/                   # API related
│   │   ├── ApiService.kt
│   │   └── dto/
│   │       └── HoldingsResponse.kt
│   ├── local/                    # Room Database
│   │   ├── entity/
│   │   ├── dao/
│   │   ├── mapper/
│   │   ├── AppDatabase.kt
│   │   └── DatabaseHelper.kt
│   └── repository/
│       └── HoldingsRepositoryImpl.kt
├── domain/                       # Domain Layer
│   ├── model/
│   │   ├── Holding.kt
│   │   └── PortfolioSummary.kt
│   ├── repository/
│   │   └── HoldingsRepository.kt
│   ├── service/
│   │   └── PortfolioCalculationService.kt
│   ├── use_case/
│   │   ├── GetHoldingsWithSummaryUseCase.kt
│   │   ├── GetHoldingsUseCase.kt
│   │   ├── GetLocalHoldingsUseCase.kt
│   │   └── SaveHoldingsUseCase.kt
│   └── validator/
│       └── HoldingValidator.kt
├── presentation/                 # Presentation Layer
│   ├── adapter/
│   │   └── HoldingsAdapter.kt
│   ├── helper/
│   │   ├── PortfolioUIHelper.kt
│   │   ├── EdgeToEdgeHelper.kt
│   │   └── NetworkUtils.kt
│   ├── model/
│   │   └── HoldingsState.kt
│   └── viewmodel/
│       └── HoldingsViewModel.kt
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
```
https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/
```

## 🎨 UI Features

- **Material Design 3**: Modern, accessible design with color-coded P&L
- **Edge-to-Edge Display**: Immersive full-screen experience
- **Expandable Summary**: Tap to expand/collapse portfolio details
- **Loading States**: Progress indicators during data fetch
- **Error Handling**: User-friendly error messages with retry option

## 🔧 Key Components

### MainActivity
- MVVM with ViewBinding and LiveData
- Edge-to-edge display setup
- Network connectivity monitoring
- Error handling and retry functionality

### HoldingsViewModel
- Manages UI state and business logic
- Network connectivity state management
- Expandable summary functionality

### Repository Pattern
- Abstraction layer for data operations
- API + Database data sources
- Offline-first approach with local caching

## 📈 Performance Optimizations

- **ViewBinding**: Type-safe view access
- **DiffUtil**: Efficient list updates
- **Coroutines**: Non-blocking async operations
- **Room Database**: Optimized local storage
- **Dagger Hilt**: Efficient dependency injection

---

# Material 3 Implementation

## Key Features
- **Color System**: Complete primary/secondary color palette with semantic colors
- **Typography**: Material 3 text styles with proper hierarchy
- **Components**: MaterialCardView, MaterialButton, CircularProgressIndicator, MaterialChip
- **Theme Support**: Light/dark themes with WCAG 2.1 AA compliance
- **Accessibility**: 48dp touch targets, proper contrast ratios

## Benefits
- Modern, consistent design
- Better user experience
- Improved accessibility
- Scalable component system

---

# Edge-to-Edge Display

## Implementation
- **Transparent System Bars**: Status and navigation bars are transparent
- **Dynamic Insets**: System bar heights handled dynamically
- **EdgeToEdgeHelper**: Reusable utility for edge-to-edge setup
- **Layout Structure**: CoordinatorLayout with proper inset handling

## Benefits
- Immersive user experience
- Modern design following Material Design 3
- Content extends to screen edges
- Maintains accessibility

---

# Network Connectivity

## Features
- **Real-time Monitoring**: Continuous network connectivity detection
- **Message Bar**: Material Design 3 error card when offline
- **Smart Retry**: Prevents unnecessary network calls when offline
- **Automatic Refresh**: Fetches data when connection restored

## Implementation
```kotlin
class NetworkUtils @Inject constructor(
    private val context: Context
) {
    fun isInternetAvailable(): Boolean
    fun getNetworkConnectivityFlow(): Flow<Boolean>
}
```

## User Experience
- **Online**: Normal operation, message bar hidden
- **Offline**: Message bar appears with retry option
- **Reconnected**: Automatic data refresh with feedback

---

# Room Database

## Features
- **Local Storage**: Holdings cached in Room database
- **Offline Support**: App works without internet using cached data
- **Reactive Updates**: Kotlin Flow for real-time data updates
- **CRUD Operations**: Full database operations with custom queries

## Usage
```kotlin
// Repository usage
@Inject lateinit var repository: HoldingsRepository
val holdings = repository.getHoldings() // With offline fallback
val localHoldings = repository.getLocalHoldings()

// Use case usage
@Inject lateinit var getLocalHoldingsUseCase: GetLocalHoldingsUseCase
val holdings = getLocalHoldingsUseCase()
```

## Data Flow
- **Online**: API → Database → UI
- **Offline**: Database → UI (with error handling)

---

# Dependency Injection

## Modules
- **AppModule**: Main coordinator module
- **NetworkModule**: Retrofit, OkHttp, NetworkUtils
- **RepositoryModule**: Repository implementations
- **UseCaseModule**: Business logic use cases
- **DatabaseModule**: Room database components

## Usage
```kotlin
@Inject lateinit var holdingsRepository: HoldingsRepository
@Inject lateinit var getHoldingsUseCase: GetHoldingsUseCase
@Inject lateinit var networkUtils: NetworkUtils
@Inject lateinit var databaseHelper: DatabaseHelper
```

## Best Practices
- Single responsibility per module
- Singleton scope for stateless dependencies
- Custom qualifiers for multiple implementations
- Testable in isolation

---

This project demonstrates a production-ready Android application following modern development practices and architectural patterns.
