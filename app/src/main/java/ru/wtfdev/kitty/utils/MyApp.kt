package ru.wtfdev.kitty.utils

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ctx = baseContext
    }

    companion object {
        lateinit var ctx: Context
    }

}