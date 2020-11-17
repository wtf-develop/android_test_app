package ru.wtfdev.kitty._navigation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.wtfdev.kitty.R


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

@AndroidEntryPoint
abstract class BaseFragment : Fragment(), IBaseFragment {
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
        hideError()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        foreground = savedInstanceState?.getBoolean("foreground", foreground) ?: foreground
        super.onCreate(savedInstanceState)
    }


    private lateinit var snackbar: Snackbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeInited = false
        snackbar = Snackbar
            .make(view, "Error", Snackbar.LENGTH_INDEFINITE)
        if (Build.VERSION.SDK_INT >= 23) {
            snackbar.setTextColor(activity?.getColor(R.color.light_text_color) ?: 0xffffff)
            snackbar.setActionTextColor(activity?.getColor(R.color.yellow_text_color) ?: 0xbfbf00)
        } else {
            snackbar.setTextColor(
                activity?.resources?.getColor(R.color.light_text_color) ?: 0xffffff
            )
            snackbar.setActionTextColor(
                activity?.resources?.getColor(R.color.yellow_text_color) ?: 0xbfbf00
            )
        }
    }

    fun showError(text: String, retry: (() -> Unit)? = null) {
        if (retry == null) {
            snackbar.setText(text.takeLast(30))
            snackbar.setAction("", null)
            snackbar.duration = Snackbar.LENGTH_LONG
        } else {
            snackbar.setText(text.takeLast(20))
            snackbar.setAction(R.string.try_again) {
                hideError()
                retry.let { it() }
            }
            snackbar.duration = Snackbar.LENGTH_INDEFINITE
        }
        snackbar.show()
    }

    fun hideError() {
        snackbar.dismiss()
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