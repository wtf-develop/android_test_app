package ru.wtfdev.kitty

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
///////@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext


    @Test
    fun enterWrongUrlErrorMessage() {
        onView(withId(R.id.my_toolbar)).check(matches(isDisplayed()))

        onView(withId(R.id.menu_item_add)).perform(click())

        onView(withHint(R.string.share_url)).check(matches(isDisplayed()))

        onView(withHint(R.string.share_url)).perform(typeText("Wrong url"))
        onView(withHint(R.string.image_title)).perform(
            typeText("Some useless name"),
            ViewActions.pressImeActionButton(),
            ViewActions.closeSoftKeyboard()
        )
        onView(withText("Wrong image")).check(matches(isDisplayed()))
    }
}