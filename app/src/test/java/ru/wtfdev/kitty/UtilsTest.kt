package ru.wtfdev.kitty

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import ru.wtfdev.kitty.utils.CloseGestureListener
import ru.wtfdev.kitty.utils.StringUtils

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsTest {
    //val context = ApplicationProvider.getApplicationContext<Context>()


    @Test
    fun randomStringGeneration() {
        var rndStr = StringUtils.randomString(100)
        assertEquals(rndStr.length, 100)
        assertEquals(rndStr.toLowerCase(), rndStr)
        rndStr = StringUtils.randomString(15)
        assertEquals(rndStr.length, 15)
        assertEquals(rndStr.toLowerCase(), rndStr)

    }

    @Test
    fun imgUrlExtract() {
        var url =
            StringUtils.extractImgUrl("hgfjkh HJGr http://rty.com/hhtp?y=76 skjb dfghoj srt https://gfkj.ty/Gutyawkjeg/hgfytwreqe.png sdlfkjht awejrht rtseurht")
        assertEquals(url, "https://gfkj.ty/Gutyawkjeg/hgfytwreqe.png")
        url =
            StringUtils.extractImgUrl("hgfjkh HJGr http://rty.com/hhtp?y=76 skjb dfghoj srt https://gfkj.ty/Gutyawkjeg/hgfytwreqe.bmp sdlfkjht awejrht rtseurht")
        assertEquals(url, "http://rty.com/hhtp?y=76")

        url =
            StringUtils.extractImgUrl("https://wtd-dev.ru/sync")
        assertEquals(url, "https://wtd-dev.ru/sync")

    }


    @Test
    fun gestureCloseDetection() {
        val callback = mock<(() -> Unit)>()
        val mockGesture = Mockito.spy(CloseGestureListener(callback))
        /** can not mock final superClass

        Mockito.doReturn(true).`when`((mockGesture as GestureDetector.SimpleOnGestureListener))
        .onFling(any(), any(), any(), any())
         */

        mockGesture.onFling(null, null, 100f, 501f)
        verify(callback, times(1)).invoke()
        mockGesture.onFling(null, null, 100f, 301f)
        verify(callback, times(2)).invoke()
        mockGesture.onFling(null, null, 100f, 250f)
        verify(callback, times(2)).invoke()
    }

}