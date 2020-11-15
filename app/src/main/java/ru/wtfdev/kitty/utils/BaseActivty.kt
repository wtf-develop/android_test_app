package ru.wtfdev.kitty.utils

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._navigation.INaviJump
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.Navigation


abstract class BaseActivty : AppCompatActivity(), INaviJump {

    abstract var startFragment: String

    lateinit var navigation: INavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComponent.create().inject(this)
        navigation = Navigation.getInstance(this)
        if (savedInstanceState == null) {
            navigation.moveTo(startFragment, false).apply {
                this?.setIsForegroung(true)
            }
        }

        //need to call onBind() and onUnBind() after changing BackStack in Fragments
        //need to call onBind() and onUnBind() after changing BackStack in Fragments
        //need to call onBind() and onUnBind() after changing BackStack in Fragments
        supportFragmentManager.addOnBackStackChangedListener {
            val count = supportFragmentManager.backStackEntryCount
            var tag = ""
            var fragment: BaseFragment? = null
            val mainFragment =
                supportFragmentManager.findFragmentByTag(startFragment) as? BaseFragment
            mainFragment?.setIsForegroung(false)
            if (count > 0) {
                for (i in 0 until count) {
                    val backEntry = supportFragmentManager.getBackStackEntryAt(i)
                    tag = backEntry.name ?: tag
                    fragment = supportFragmentManager.findFragmentByTag(tag) as? BaseFragment
                    fragment?.setIsForegroung(false)
                    if (i < (count - 1)) {
                        fragment?.onUnsubscribeBindings()
                    }
                }
                mainFragment?.onUnsubscribeBindings()
            } else {
                tag = startFragment
                fragment = mainFragment
            }
            fragment?.setIsForegroung(true)
            fragment?.onSubscribeBindings()
            title = getString(navigation.getTitle(tag))
            supportActionBar?.setDisplayHomeAsUpEnabled(getBackButton())
        }
    }

    private fun getBackButton(): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            return true
        }
        return false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val b = super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                navigation.popBackStack()
            }
        }
        return b
    }

    override fun getNaviFragmentManager(): FragmentManager {
        return supportFragmentManager
    }
}