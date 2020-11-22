package ru.wtfdev.kitty

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.serialization.json.Json
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty._models.repo.implementation.LocalStorageRepository


private const val FAKE_STRING = "HELLO_WORLD"

@Config(sdk = [28]) //I have now java 8 only
@RunWith(RobolectricTestRunner::class)
class RobolectricTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val storageTest: ILocalStorageRepository = LocalStorageRepository(context, Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    })

    @Test
    fun readStringFromContext_LocalizedString() {

        val uuid = storageTest.getUUID()
        assertTrue(uuid.length >= 15)

        storageTest.storeDailyList(
            listOf(
                ItemModel(1, "link", "title", 1, 1, 1),
                ItemModel(2, "link2", "title2", 2, 2, 2)
            )
        )
        val list = storageTest.getDailyList()
        assertNotNull(list)
        assertTrue(list?.size == 2)
        assertTrue(list?.get(0)?.link.equals("link"))


    }

}