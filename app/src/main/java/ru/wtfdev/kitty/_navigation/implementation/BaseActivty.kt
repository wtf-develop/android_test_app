package ru.wtfdev.kitty._navigation.implementation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.INavigationProvider
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivty : AppCompatActivity(), INavigationProvider {

    abstract var startFragment: String


    @Inject
    lateinit var navigation: INavigation

    var backEntryCount=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigation.push(startFragment, false).apply {
                this?.setIsForegroung(true)
            }
            title = navigation.getTitle()
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
            if(backEntryCount>count){
                navigation.pop(auto = true)
            }
            backEntryCount=count
            fragment?.setIsForegroung(true)
            fragment?.onSubscribeBindings()
            title = navigation.getTitle()
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
                navigation.pop()
            }
        }
        return b
    }

    override fun getNaviFragmentManager(): FragmentManager {
        return supportFragmentManager
    }
}