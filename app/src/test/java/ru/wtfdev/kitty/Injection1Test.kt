package ru.wtfdev.kitty

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class Injection1Test {

    //TODO soon

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
}