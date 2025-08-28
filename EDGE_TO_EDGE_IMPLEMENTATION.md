# Edge-to-Edge Display Implementation

This document explains the edge-to-edge display implementation in the PulkitNigamTask Android application.

## Overview

Edge-to-edge display allows the app content to extend behind the system bars (status bar and navigation bar), providing a more immersive user experience while ensuring content remains accessible.

## Implementation Details

### 1. Theme Configuration

The app uses transparent system bars in both light and dark themes:

**Light Theme (`values/themes.xml`):**
```xml
<item name="android:statusBarColor">@android:color/transparent</item>
<item name="android:navigationBarColor">@android:color/transparent</item>
<item name="android:windowLightStatusBar">false</item>
<item name="android:windowLightNavigationBar" tools:targetApi="27">false</item>
```

**Dark Theme (`values-night/themes.xml`):**
Similar configuration with appropriate icon colors for dark mode.

### 2. MainActivity Setup

The MainActivity uses the `EdgeToEdgeHelper` utility class to handle edge-to-edge display:

```kotlin
private fun setupEdgeToEdge() {
    EdgeToEdgeHelper.setupEdgeToEdge(
        activity = this,
        rootView = binding.root,
        appBarLayout = binding.appBarLayout,
        contentContainer = binding.mainContentContainer
    )
}
```

### 3. Layout Structure

The layout uses a CoordinatorLayout with proper IDs for edge-to-edge handling:

- `root`: The main CoordinatorLayout
- `appBarLayout`: The AppBarLayout that handles status bar insets
- `mainContentContainer`: The main content area that handles navigation bar insets

### 4. EdgeToEdgeHelper Utility

The `EdgeToEdgeHelper` class provides reusable functionality:

- `enableEdgeToEdge()`: Enables edge-to-edge display and configures system bars
- `setupSystemInsets()`: Handles dynamic system insets
- `setupEdgeToEdge()`: Combines both functions for easy setup

## Key Features

1. **Dynamic Insets**: System bar heights are handled dynamically rather than using hardcoded values
2. **Transparent System Bars**: Status and navigation bars are transparent
3. **Proper Content Positioning**: Content is positioned to avoid system bars
4. **Dark/Light Mode Support**: Proper icon colors for both themes
5. **Reusable Implementation**: EdgeToEdgeHelper can be used in other activities

## Usage

To implement edge-to-edge display in a new activity:

1. Ensure the activity theme has transparent system bars
2. Use the EdgeToEdgeHelper in onCreate():
```kotlin
EdgeToEdgeHelper.setupEdgeToEdge(
    activity = this,
    rootView = binding.root,
    appBarLayout = binding.appBarLayout, // optional
    contentContainer = binding.contentContainer // optional
)
```

## Benefits

- **Immersive Experience**: Content extends to the edges of the screen
- **Modern Design**: Follows Material Design 3 guidelines
- **Accessibility**: Content remains accessible despite edge-to-edge display
- **Consistency**: Uniform implementation across the app
- **Maintainability**: Centralized edge-to-edge logic

## Notes

- The implementation uses `WindowCompat` APIs for backward compatibility
- System insets are handled dynamically to support different device configurations
- The app bar automatically handles status bar insets
- Content containers handle navigation bar insets
