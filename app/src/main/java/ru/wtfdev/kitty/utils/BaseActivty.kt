package ru.wtfdev.kitty.utils

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.list.ImageListView

abstract class BaseActivty : AppCompatActivity() {

    abstract var startFragment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            moveTo(startFragment, false).apply {
                this?.setIsForegroung(true)
            }
        }
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
            title = getString(getTitle(tag))
            supportActionBar?.setDisplayHomeAsUpEnabled(getBackButton())
        }
    }

    private fun getBackButton(): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            return true
        }
        return false
    }


    protected fun getTitle(tag: String): Int {
        when (tag) {
            ImageListView::class.simpleName -> return R.string.app_name
            DetailsView::class.simpleName -> return R.string.details
        }
        return R.string.error
    }


    protected fun moveTo(tag: String, backstack: Boolean = true): IBaseFragment? {
        var fragment: BaseFragment?
        when (tag) {
            ImageListView::class.simpleName -> fragment = ImageListView.newInstance()
            DetailsView::class.simpleName -> fragment = DetailsView.newInstance("id")
            else -> return null
        }
        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragments, fragment, tag)
        if (backstack) {
            transaction.addToBackStack(tag)
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        transaction.commit()
        return fragment
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val b = super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            }
        }
        return b
    }
}