package ru.wtfdev.kitty._models.repo

import android.content.Context
import android.provider.Settings
import ru.wtfdev.kitty.utils.StringUtils


interface ILocalStorageRepository {
    fun getUUID(): String
}

class LocalStorageRepository constructor(val ctx: Context) : ILocalStorageRepository {

    private val PREF_NAME = "local_storage"
    private val INSTALLATION_ID = "installation_id"


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

}