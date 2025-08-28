# Dependency Injection Package

This package contains all Dagger Hilt modules and qualifiers for the application's dependency injection setup.

## 📁 Package Structure

```
di/
├── modules/
│   ├── AppModule.kt              # Main application module (coordinator)
│   ├── data/
│   │   ├── NetworkModule.kt      # Networking dependencies
│   │   ├── RepositoryModule.kt   # Repository implementations
│   │   └── DatabaseModule.kt     # Database dependencies (future)
│   └── domain/
│       └── UseCaseModule.kt      # Use case dependencies
├── keys/
│   └── Qualifiers.kt             # Custom qualifiers and annotations
└── README.md                     # This documentation
```

## 🏗️ Module Overview

### AppModule
- **Purpose**: Main coordinator module that includes all other modules
- **Scope**: Singleton
- **Dependencies**: Includes NetworkModule, RepositoryModule, UseCaseModule, DatabaseModule

### NetworkModule (data)
- **Purpose**: Provides all networking-related dependencies
- **Scope**: Singleton
- **Provides**:
  - `HttpLoggingInterceptor` - Network request/response logging
  - `OkHttpClient` - HTTP client with interceptors and timeouts
  - `Retrofit` - REST API client
  - `ApiService` - API interface implementation

### RepositoryModule (data)
- **Purpose**: Maps repository interfaces to their implementations
- **Scope**: Singleton
- **Provides**:
  - `HoldingsRepository` - Portfolio holdings data access

### UseCaseModule (domain)
- **Purpose**: Provides business logic use cases
- **Scope**: Singleton
- **Provides**:
  - `GetHoldingsUseCase` - Fetch portfolio holdings
  - `GetPortfolioSummaryUseCase` - Calculate portfolio summary

### DatabaseModule (data)
- **Purpose**: Database-related dependencies (future use)
- **Scope**: Singleton
- **Provides**: Currently empty, ready for Room database integration

## 🏷️ Qualifiers

Custom qualifiers for type-safe dependency injection:

- `@ApiBaseUrl` - Distinguish between different API base URLs
- `@NetworkTimeout` - Different timeout configurations
- `@LoggingLevel` - Different logging levels
- `@DatabaseName` - Different database names

## 🔧 Usage Examples

### Injecting a Repository
```kotlin
@Inject
lateinit var holdingsRepository: HoldingsRepository
```

### Injecting Use Cases
```kotlin
@Inject
lateinit var getHoldingsUseCase: GetHoldingsUseCase

@Inject
lateinit var getPortfolioSummaryUseCase: GetPortfolioSummaryUseCase
```

### Injecting Network Components
```kotlin
@Inject
lateinit var apiService: ApiService
```

## 🚀 Adding New Dependencies

### 1. Network Dependencies
Add to `modules/data/NetworkModule.kt`:
```kotlin
@Provides
@Singleton
fun provideNewNetworkComponent(): NewNetworkComponent {
    return NewNetworkComponent()
}
```

### 2. Repository Dependencies
Add to `modules/data/RepositoryModule.kt`:
```kotlin
@Provides
@Singleton
fun provideNewRepository(apiService: ApiService): NewRepository {
    return NewRepositoryImpl(apiService)
}
```

### 3. Use Case Dependencies
Add to `modules/domain/UseCaseModule.kt`:
```kotlin
@Provides
@Singleton
fun provideNewUseCase(repository: NewRepository): NewUseCase {
    return NewUseCase(repository)
}
```

### 4. Database Dependencies
Add to `modules/data/DatabaseModule.kt`:
```kotlin
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "database_name").build()
}
```

### 5. New Qualifiers
Add to `keys/Qualifiers.kt`:
```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewQualifier
```

## 🔒 Best Practices

1. **Single Responsibility**: Each module should handle one specific concern
2. **Singleton Scope**: Use `@Singleton` for stateless dependencies
3. **Qualifiers**: Use custom qualifiers when multiple implementations exist
4. **Documentation**: Document all provider methods with KDoc
5. **Testing**: Each module should be testable in isolation
6. **Package Organization**: Group modules by layer (data, domain, presentation)

## 📈 Future Enhancements

- [ ] Add Room database integration
- [ ] Add caching strategies
- [ ] Add different environment configurations (dev/staging/prod)
- [ ] Add network security configurations
- [ ] Add analytics and crash reporting dependencies
- [ ] Add presentation layer modules (ViewModel, Adapter, etc.)
- [ ] Add utility modules for common dependencies
