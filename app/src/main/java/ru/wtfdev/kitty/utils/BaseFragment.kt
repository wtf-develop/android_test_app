package ru.wtfdev.kitty.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment


//Interface for Navigation object
//Interface for Navigation object
//Interface for Navigation object
interface IBaseFragment {
    fun setIsForegroung(b: Boolean)
    fun onSubscribeBindings()
    fun onUnsubscribeBindings()
}


//Fragment implementation
//Fragment implementation
//Fragment implementation
open abstract class BaseFragment : Fragment(), IBaseFragment {
    var subscribeInited = false
    var startedOnce = false
    private var foreground = false

    protected abstract fun onDataBing()
    protected abstract fun onDataUnBing()


    override fun setIsForegroung(b: Boolean) {
        foreground = b
    }

    override fun onSubscribeBindings() {
        if (!foreground) return
        if (!startedOnce) return
        if (subscribeInited) return
        subscribeInited = true
        onDataBing()
    }

    override fun onUnsubscribeBindings() {
        if (!subscribeInited) return
        subscribeInited = false
        onDataUnBing()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        foreground = savedInstanceState?.getBoolean("foreground", foreground) ?: foreground
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeInited = false
    }

    override fun onStart() {
        super.onStart()
        startedOnce = true
        onSubscribeBindings()
    }

    override fun onStop() {
        super.onStop()
        onUnsubscribeBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onUnsubscribeBindings()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("foreground", foreground)
        super.onSaveInstanceState(outState)
    }
}