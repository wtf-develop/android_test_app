package ru.wtfdev.kitty._models.repo

import android.content.Context
import android.provider.Settings
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty.utils.StringUtils


interface ILocalStorageRepository {
    fun getUUID(): String
    fun storeDailyList(listdata: List<ItemModel>)
    fun getDailyList(): List<ItemModel>?
}

class LocalStorageRepository constructor(val ctx: Context, val parser: Json) :
    ILocalStorageRepository {

    private val PREF_NAME = "local_storage"
    private val INSTALLATION_ID = "installation_id"

    private val LIST_STORAGE = "daily_list"
    private val LIST_DATA = "daily_list_data"
    private val LIST_TIME = "daily_list_time"
    private val LIST_DELAY = 10 * 60 * 60 * 1000L


    private var uniqInstallId: String? = null
    override fun getUUID(): String {
        uniqInstallId?.let {
            if (it.length > 10) return it
        }
        if ((uniqInstallId?.length ?: 0) <= 10) {//try to load from storage
            val pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            uniqInstallId = pref.getString(INSTALLATION_ID, "")
            if ((uniqInstallId?.length ?: 0) <= 10) {//if nothing in storage - create it
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
                if ((uniqInstallId?.length ?: 0) <= 10) {
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

}