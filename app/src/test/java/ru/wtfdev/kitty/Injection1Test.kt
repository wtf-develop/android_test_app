package ru.wtfdev.kitty

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [28])
class Injection1Test {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var injectedStorage: ILocalStorageRepository

    @Before
    fun init() {
        /**
         * Until bug is not fixed
         * URL: https://issuetracker.google.com/issues/37076369
         * - hiltRule.inject()
         * is necessary for normal work of injection
         * tests from Android Studio UI.
         */
        hiltRule.inject()
    }

    @Test
    fun testInjectedStorage() {
        val uuid = injectedStorage.getUUID()
        Assert.assertTrue(uuid.length >= 15)

        injectedStorage.storeDailyList(
            listOf(
                ItemModel(1, "link", "title", 1, 1, 1),
                ItemModel(2, "link2", "title2", 2, 2, 2)
            )
        )
        val list = injectedStorage.getDailyList()
        Assert.assertNotNull(list)
        Assert.assertTrue(list?.size == 2)
        Assert.assertTrue(list?.get(0)?.link.equals("link"))
        Assert.assertTrue(list?.get(1)?.link.equals("link2"))

        Assert.assertFalse(injectedStorage.checkAbuse(100))
        Assert.assertTrue(injectedStorage.toggleAbuse(100))
        Assert.assertTrue(injectedStorage.checkAbuse(100))
    }

}