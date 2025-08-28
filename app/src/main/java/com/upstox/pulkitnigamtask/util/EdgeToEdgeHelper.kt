package com.upstox.pulkitnigamtask.util

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Utility class to handle edge-to-edge display functionality
 */
object EdgeToEdgeHelper {
    
    /**
     * Enables edge-to-edge display for the given activity
     */
    fun enableEdgeToEdge(activity: Activity) {
        val window = activity.window
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Configure system bars
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.apply {
            // Make status bar icons light (for dark backgrounds)
            isAppearanceLightStatusBars = false
            // Make navigation bar icons light (for dark backgrounds)
            isAppearanceLightNavigationBars = false
        }
    }
    
    /**
     * Sets up system insets handling for edge-to-edge display
     * @param rootView The root view of the activity
     * @param appBarLayout The app bar layout (if any)
     * @param contentContainer The main content container
     */
    fun setupSystemInsets(
        rootView: View,
        appBarLayout: View? = null,
        contentContainer: View? = null
    ) {
        rootView.setOnApplyWindowInsetsListener { view, windowInsets ->
            val insets = WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Apply padding to the app bar to account for status bar
            appBarLayout?.updatePadding(top = insets.top)
            
            // Apply padding to the content container
            contentContainer?.updatePadding(
                top = 0, // Remove top padding since AppBarLayout handles it
                bottom = insets.bottom
            )
            
            windowInsets
        }
    }
    
    /**
     * Sets up edge-to-edge display with automatic insets handling
     */
    fun setupEdgeToEdge(
        activity: Activity,
        rootView: View,
        appBarLayout: View? = null,
        contentContainer: View? = null
    ) {
        enableEdgeToEdge(activity)
        setupSystemInsets(rootView, appBarLayout, contentContainer)
    }
}
