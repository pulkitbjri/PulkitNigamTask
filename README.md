# PulkitNigamTask - Portfolio Holdings App

A modern Android application built with MVVM + Clean Architecture, demonstrating best practices for scalable and maintainable codebase.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVVM** pattern:

### Layers:
1. **Presentation Layer (UI)**
   - Activities, Fragments, ViewModels
   - XML layouts with ViewBinding
   - Material Design 3 components

2. **Domain Layer (Business Logic)**
   - Use Cases
   - Domain Models
   - Repository Interfaces

3. **Data Layer**
   - Repository Implementations
   - Remote Data Sources (API)
   - Data Transfer Objects (DTOs)

## 🛠️ Tech Stack

- **Language**: Kotlin 2.0.21
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt 2.52
- **Networking**: Retrofit 2.12.0 + OkHttp 4.12.0
- **UI**: XML Layouts (No Compose)
- **Async Operations**: Kotlin Coroutines & Flow
- **Testing**: JUnit, MockK, Mockito
- **Build System**: Gradle with Kotlin DSL

## 📱 Features

- **Holdings List**: Display portfolio holdings with real-time data
- **Portfolio Summary**: Expandable/collapsible summary with calculated values
- **Real-time P&L**: Color-coded profit/loss indicators
- **Offline Support**: Error handling and retry functionality
- **Material Design**: Modern UI following Material Design 3 guidelines

## 🏛️ SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- Each class has a single, well-defined responsibility
- Use Cases handle specific business operations
- Repository handles data operations
- ViewModels manage UI state

### Open/Closed Principle (OCP)
- Repository interfaces allow different implementations
- Use Cases are open for extension through dependency injection

### Liskov Substitution Principle (LSP)
- Repository implementations are interchangeable
- Domain models are independent of data layer

### Interface Segregation Principle (ISP)
- Repository interface contains only necessary methods
- Use Cases have focused interfaces

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions
- Dagger Hilt provides dependency injection

## 📊 Portfolio Calculations

The app implements the required calculations:

1. **Current Value** = Σ(LTP × Net Quantity)
2. **Total Investment** = Σ(Average Price × Net Quantity)  
3. **Total P&L** = Current Value - Total Investment
4. **Today's P&L** = Σ((Close - LTP) × Net Quantity)

## 🧪 Testing

- **Unit Tests**: Business logic in Use Cases and Repository
- **Test Coverage**: Core calculations and data transformations
- **Mocking**: API responses and dependencies
- **Test Frameworks**: JUnit, MockK, Mockito

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
│   └── repository/
│       └── HoldingsRepositoryImpl.kt
├── domain/                       # Domain Layer
│   ├── exception/
│   │   └── DomainException.kt
│   ├── model/
│   │   ├── Holding.kt
│   │   └── PortfolioSummary.kt
│   ├── repository/
│   │   └── HoldingsRepository.kt
│   ├── service/
│   │   └── PortfolioCalculationService.kt
│   ├── use_case/
│   │   ├── GetHoldingsWithSummaryUseCase.kt
│   │   └── GetHoldingsUseCase.kt
│   └── validator/
│       └── HoldingValidator.kt
├── presentation/                 # Presentation Layer
│   ├── adapter/
│   │   └── HoldingsAdapter.kt
│   ├── helper/
│   │   └── PortfolioUIHelper.kt
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

### HoldingsViewModel
- Manages UI state and business logic
- Handles data loading and error states
- Implements expandable summary functionality

### HoldingsAdapter
- RecyclerView adapter with DiffUtil
- Efficient list updates with smooth animations
- Color-coded P&L display

### Repository Pattern
- Abstraction layer for data operations
- Supports multiple data sources
- Easy to test and maintain

## 📈 Performance Optimizations

- **ViewBinding**: Type-safe view access
- **DiffUtil**: Efficient list updates
- **Coroutines**: Non-blocking async operations
- **RecyclerView**: Memory-efficient list rendering
- **Dagger Hilt**: Efficient dependency injection

## 🔒 Error Handling

- Network error handling
- API error responses
- Offline state management
- User-friendly error messages
- Retry functionality

This project demonstrates a production-ready Android application following modern development practices and architectural patterns.
