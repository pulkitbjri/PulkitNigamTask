# Room Database Implementation

This document explains the Room database implementation in the PulkitNigamTask project.

## Overview

The project now includes a complete Room database implementation for local data storage and caching. The database is designed to work with the existing Clean Architecture pattern and provides offline support for the holdings data.

## Architecture

### Database Structure

```
data/local/
├── entity/
│   └── HoldingEntity.kt          # Room entity for holdings
├── dao/
│   └── HoldingDao.kt             # Data Access Object
├── mapper/
│   └── HoldingMapper.kt          # Domain-Entity mapper
├── AppDatabase.kt                # Main database class
└── DatabaseHelper.kt             # Utility helper class
```

### Key Components

1. **HoldingEntity**: Room entity representing holdings in the database
2. **HoldingDao**: Interface for database operations (CRUD, queries)
3. **HoldingMapper**: Converts between domain models and entities
4. **AppDatabase**: Main database class with Room configuration
5. **DatabaseHelper**: Utility class for common operations

## Features

### ✅ Implemented Features

- **Local Data Storage**: Holdings are stored locally using Room database
- **Offline Support**: App can work offline using cached data
- **Reactive Updates**: Uses Kotlin Flow for reactive data updates
- **CRUD Operations**: Full Create, Read, Update, Delete operations
- **Custom Queries**: Filtering for profitable/loss-making holdings
- **Dependency Injection**: Integrated with Dagger Hilt
- **Clean Architecture**: Follows domain-driven design principles

### Database Operations

#### Basic CRUD
- Insert holdings (single or multiple)
- Read all holdings
- Update holdings
- Delete holdings
- Get holdings by symbol

#### Advanced Queries
- Get profitable holdings (positive P&L)
- Get loss-making holdings (negative P&L)
- Get holdings count
- Check if database is empty

## Usage Examples

### 1. Using Repository (Recommended)

```kotlin
@Inject
lateinit var repository: HoldingsRepository

// Get holdings with offline fallback
val holdings = repository.getHoldings()

// Get local holdings only
val localHoldings = repository.getLocalHoldings()

// Save holdings to database
repository.saveHoldings(holdingsList)

// Get profitable holdings
val profitable = repository.getProfitableHoldings()
```

### 2. Using Use Cases

```kotlin
@Inject
lateinit var getLocalHoldingsUseCase: GetLocalHoldingsUseCase
@Inject
lateinit var saveHoldingsUseCase: SaveHoldingsUseCase

// Get local holdings
val holdings = getLocalHoldingsUseCase()

// Save holdings
saveHoldingsUseCase(holdingsList)
```

### 3. Using Database Helper

```kotlin
@Inject
lateinit var databaseHelper: DatabaseHelper

// Get all holdings
val holdings = databaseHelper.getAllHoldings()

// Save holdings
databaseHelper.saveHoldings(holdingsList)

// Check if database is empty
val isEmpty = databaseHelper.isDatabaseEmpty()
```

### 4. Direct DAO Access

```kotlin
@Inject
lateinit var holdingDao: HoldingDao

// Get all holdings as Flow
val holdingsFlow = holdingDao.getAllHoldings()

// Insert holdings
holdingDao.insertHoldings(entities)

// Get profitable holdings
val profitable = holdingDao.getProfitableHoldings()
```

## Database Configuration

### Room Setup

The database is configured in `AppDatabase.kt`:

```kotlin
@Database(
    entities = [HoldingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
```

### Dependencies

Room dependencies are already configured in `build.gradle.kts`:

```kotlin
// Room Database
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
kapt(libs.androidx.room.compiler)
```

### Dependency Injection

Database dependencies are provided through Dagger Hilt in `DatabaseModule.kt`:

```kotlin
@Provides
@Singleton
fun provideDatabase(@ApplicationContext context: Context): AppDatabase

@Provides
@Singleton
fun provideHoldingDao(database: AppDatabase): HoldingDao
```

## Data Flow

### Online Scenario
1. API call fetches data from remote server
2. Data is automatically saved to local database
3. UI receives data from API response
4. Database is updated for offline access

### Offline Scenario
1. API call fails
2. Repository automatically falls back to local database
3. UI receives cached data from database
4. App continues to work offline

## Migration Strategy

The database uses `fallbackToDestructiveMigration()` for simplicity. In production, you should implement proper migration strategies:

```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, "database_name")
    .addMigrations(MIGRATION_1_2) // Add proper migrations
    .build()
```

## Testing

### Unit Testing

You can test database operations using Room's in-memory database:

```kotlin
@RunWith(AndroidJUnit4::class)
class HoldingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: HoldingDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.holdingDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndReadHolding() {
        // Test implementation
    }
}
```

## Best Practices

1. **Always use Repository pattern**: Don't access DAO directly from UI
2. **Use Flow for reactive updates**: Ensures UI stays in sync with database
3. **Handle errors gracefully**: Implement proper error handling for database operations
4. **Use suspend functions**: For database operations to avoid blocking main thread
5. **Implement proper migrations**: For production apps with schema changes
6. **Test database operations**: Use in-memory database for testing

## Future Enhancements

- [ ] Add database migrations for schema changes
- [ ] Implement database encryption for sensitive data
- [ ] Add database backup and restore functionality
- [ ] Implement database performance monitoring
- [ ] Add database analytics and usage statistics

## Troubleshooting

### Common Issues

1. **Database not found**: Ensure database name is correct in `AppDatabase`
2. **Migration errors**: Check database version and migration strategy
3. **Performance issues**: Use appropriate indexes and optimize queries
4. **Memory leaks**: Ensure database is properly closed in tests

### Debug Tips

- Enable Room logging in debug builds
- Use Android Studio's Database Inspector
- Monitor database file size and performance
- Check for memory leaks using LeakCanary
