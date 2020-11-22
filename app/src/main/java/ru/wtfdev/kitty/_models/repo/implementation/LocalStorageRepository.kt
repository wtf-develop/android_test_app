package ru.wtfdev.kitty._models.repo.implementation

import android.content.Context
import android.provider.Settings
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._models.repo.ILocalStorageRepository
import ru.wtfdev.kitty.utils.StringUtils
import java.util.*


class LocalStorageRepository constructor(val ctx: Context, val parser: Json) :
    ILocalStorageRepository {

    private val PREF_NAME = "local_storage"
    private val INSTALLATION_ID = "installation_id"

    private val LIST_STORAGE = "daily_list"
    private val LIST_DATA = "daily_list_data"
    private val LIST_TIME = "daily_list_time"
    private val LIST_DELAY = 10 * 60 * 60 * 1000L


    private val LIKES = "likes"
    private val DISLIKES = "dislikes"
    private val ABUSE = "abuse"


    private var uniqInstallId: String? = null
    override fun getUUID(): String {
        uniqInstallId?.let {
            if (it.length > 10) return it
        }
        if ((uniqInstallId?.length ?: 0) <= 10) {//try to load from storage
            val pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            uniqInstallId = pref.getString(INSTALLATION_ID, "")
            if ((uniqInstallId?.length ?: 0) < 15) {//if nothing in storage - create it
                try {//try to use DEVICEID
                    uniqInstallId =
                        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
                    uniqInstallId =
                        uniqInstallId?.replace("-", "")?.replace(":", "")?.replace(".", "")
                            ?.toLowerCase()
                } catch (_: Exception) {
                    uniqInstallId = ""
                }
                //if deviceID is bad - create it as random string
                if ((uniqInstallId?.length ?: 0) < 15) {
                    uniqInstallId = StringUtils.randomString(15)
                }
                val ed = pref.edit()
                ed.putString(INSTALLATION_ID, uniqInstallId)
                ed.apply()
            }
        }
        return uniqInstallId ?: ""
    }

    override fun storeDailyList(listdata: List<ItemModel>) {
        val js = parser.encodeToString(ListSerializer(ItemModel.serializer()), listdata)
        val pref = ctx.getSharedPreferences(LIST_STORAGE, Context.MODE_PRIVATE)
        val ed = pref.edit()
        val time = System.currentTimeMillis()
        ed.putString(LIST_DATA, js)
        ed.putLong(LIST_TIME, time)
        ed.apply()
        data = listdata
        data_time = time

    }

    var data: List<ItemModel>? = null
    var data_time = 0L
    override fun getDailyList(): List<ItemModel>? {
        if (data == null || (Math.abs(data_time - System.currentTimeMillis()) > LIST_DELAY)) {
            val pref = ctx.getSharedPreferences(LIST_STORAGE, Context.MODE_PRIVATE)
            data_time = pref.getLong(LIST_TIME, 0L)
            if (Math.abs(data_time - System.currentTimeMillis()) > LIST_DELAY) {
                return null
            }
            val js = pref.getString(LIST_DATA, "")
            if (js.isNullOrEmpty()) {
                return null
            }
            var result: List<ItemModel>? = null
            try {
                result = parser.decodeFromString(ListSerializer(ItemModel.serializer()), js)
                data = result
            } catch (_: Exception) {
            }
            return result
        } else {
            return data
        }
    }

    private fun storeArray(key: String, list: List<Int>) {
        val prefs = ctx.getSharedPreferences(key, Context.MODE_PRIVATE)
        val str = StringBuilder()
        for (i in list.takeLast(250).indices) {
            str.append(list[i]).append(",")
        }
        prefs.edit().putString(key, str.toString()).apply()
    }

    private fun loadArray(key: String): MutableList<Int> {
        val prefs = ctx.getSharedPreferences(key, Context.MODE_PRIVATE)
        val savedString: String = prefs.getString(key, "") ?: ""
        val st = StringTokenizer(savedString, ",")
        var counter = 250
        val intArr = mutableListOf<Int>()
        while (true) {
            counter--
            if (counter < 1) return intArr
            if (!st.hasMoreTokens()) return intArr
            val intValue = st.nextToken().toIntOrNull()
            if (intValue == null) return intArr
            intArr.add(intValue)
        }
        return intArr
    }

    val likes = loadArray(LIKES)
    override fun toggleLike(id: Int): Boolean {
        var removed = likes.indexOf(id)
        var result = true
        if (removed > -1) {
            likes.removeAt(removed)
            result = false
        } else {
            likes.add(id)
        }
        storeArray(LIKES, likes)
        return result
    }

    val dislikes = loadArray(DISLIKES)
    override fun toggleDislike(id: Int): Boolean {
        var removed = dislikes.indexOf(id)
        var result = true
        if (removed > -1) {
            dislikes.removeAt(removed)
            result = false
        } else {
            dislikes.add(id)
        }
        storeArray(DISLIKES, dislikes)
        return result
    }

    val abuse = loadArray(ABUSE)
    override fun toggleAbuse(id: Int): Boolean {
        var removed = abuse.indexOf(id)
        var result = true
        if (removed > -1) {
            abuse.removeAt(removed)
            result = false
        } else {
            abuse.add(id)
        }
        storeArray(ABUSE, abuse)
        return result
    }

    override fun checkLike(id: Int): Boolean {
        return likes.any { it == id }
    }

    override fun checkDislike(id: Int): Boolean {
        return dislikes.any { it == id }
    }

    override fun checkAbuse(id: Int): Boolean {
        return abuse.any { it == id }
    }

}