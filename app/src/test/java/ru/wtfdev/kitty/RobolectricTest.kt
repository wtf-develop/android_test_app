package ru.wtfdev.kitty

//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty._models.repo.implementation.LocalStorageRepository
import ru.wtfdev.kitty._navigation.INavigationProvider
import ru.wtfdev.kitty._navigation.implementation.Navigation


@Config(sdk = [28]) //I have now java8 only
@RunWith(RobolectricTestRunner::class)
class RobolectricTest {

    val context = ApplicationProvider.getApplicationContext<Context>()
    val storageTest: ILocalStorageRepository = LocalStorageRepository(context, Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    })

    @Test
    fun checkDataStorageLogic() {

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
        assertTrue(list?.get(1)?.link.equals("link2"))

        assertFalse(storageTest.checkAbuse(100))
        assertTrue(storageTest.toggleAbuse(100))
        assertTrue(storageTest.checkAbuse(100))
    }


    @Test
    fun navigationPathTest() {
        val provider = mock<INavigationProvider>()
        Mockito.`when`(provider.getNaviFragmentManager()).thenReturn(null)
        val navigation = Navigation(provider, context)
        navigation.push("/list")
        navigation.push("/list")
        navigation.push("/list")
        navigation.push("/list")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.main_title)))
        navigation.push("/details")
        navigation.push("/details")
        navigation.push("/details")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.push("/add")
        navigation.push("/add")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.add_link_view)))
        navigation.push("/details")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.push("/list")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.main_title)))

        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.add_link_view)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.add_link_view)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.pop(true)
        navigation.pop(true)
        navigation.pop(true)
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.main_title)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.error)))
        navigation.pop(true)
        navigation.pop(true)
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.error)))
        navigation.push("/add")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.add_link_view)))
        navigation.push("/details", backstack = false)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.push("/list")
        assertTrue(navigation.getTitle().equals(context.getString(R.string.main_title)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.details)))
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.error)))
        navigation.pop(true)
        navigation.pop(true)
        assertTrue(navigation.getTitle().equals(context.getString(R.string.error)))
    }

}