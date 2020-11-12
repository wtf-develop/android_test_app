package ru.wtfdev.kitty.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    var subscribeInited = false
    var startedOnce = false

    protected abstract fun onDataBing()
    protected abstract fun onDataUnBing()


    fun onSubscribeBindings() {
        if (!startedOnce) return
        if (subscribeInited) return
        subscribeInited = true
        onDataBing()
    }

    fun onUnsubscribeBindings() {
        if (!subscribeInited) return
        subscribeInited = false
        onDataUnBing()
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
}