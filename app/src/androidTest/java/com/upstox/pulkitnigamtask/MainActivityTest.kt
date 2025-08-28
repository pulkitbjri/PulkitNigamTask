package com.upstox.pulkitnigamtask

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testMainActivityLaunches() {
        ActivityScenario.launch(MainActivity::class.java)
        
        // Verify toolbar title is displayed
        onView(withText("Portfolio"))
            .check(matches(isDisplayed()))
        
        // Verify holdings header is present
        onView(withText("HOLDINGS"))
            .check(matches(isDisplayed()))
        
        // Verify portfolio summary section is present
        onView(withText("Profit & Loss*"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testPortfolioSummaryExpandCollapse() {
        ActivityScenario.launch(MainActivity::class.java)
        
        // Initially, summary details should be collapsed
        onView(withId(R.id.layoutSummaryDetails))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        
        // Click on portfolio summary to expand
        onView(withId(R.id.layoutPortfolioSummary))
            .perform(click())
        
        // After clicking, summary details should be visible
        onView(withId(R.id.layoutSummaryDetails))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        
        // Click again to collapse
        onView(withId(R.id.layoutPortfolioSummary))
            .perform(click())
        
        // After clicking again, summary details should be hidden
        onView(withId(R.id.layoutSummaryDetails))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}
