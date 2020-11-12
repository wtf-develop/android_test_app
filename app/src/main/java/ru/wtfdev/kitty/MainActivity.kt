package ru.wtfdev.kitty

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.detail.IDetailsViewModel
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.BaseFragment


interface INavigation {
    fun toList()
    fun toDetails()
    fun getImageListVModel(): IImageListViewModel
    fun getDetailsVModel(): IDetailsViewModel
}

class MainActivity : AppCompatActivity(), INavigation {
    private var topFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            moveTo("startpage", false)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val count = supportFragmentManager.backStackEntryCount
            var tag = ""
            if (count > 0) {
                topFragment?.onUnsubscribeBindings()
                val backEntry = supportFragmentManager.getBackStackEntryAt(count - 1)
                tag = backEntry.name ?: ""
                val fragment = supportFragmentManager.findFragmentByTag(tag) as? BaseFragment
                topFragment = fragment
                topFragment?.onSubscribeBindings()
            }
            title = getTitle(tag)
            supportActionBar?.setDisplayHomeAsUpEnabled(getBackButton(tag))
        }
    }

    override fun toList() {
        moveTo(ImageListView.tag, false)
    }

    override fun toDetails() {
        moveTo(DetailsView.tag)
    }


    private fun moveTo(tag: String, backstack: Boolean = true) {
        var fragment: BaseFragment?
        when (tag.toLowerCase()) {
            ImageListView.tag -> fragment = ImageListView.newInstance()
            DetailsView.tag -> fragment = DetailsView.newInstance("id")
            else -> return
        }
        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragments, fragment, tag)
        if (backstack) {
            transaction.addToBackStack(tag)
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        transaction.commit()
        topFragment = fragment

    }

    private fun getTitle(tag: String): String {
        when (tag.toLowerCase()) {
            ImageListView.tag -> return "Kitty app"
            DetailsView.tag -> return "Details about kitty"
        }
        return "Error"
    }

    private fun getBackButton(tag: String): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            return true
        }
        return false
    }

    override fun getImageListVModel(): IImageListViewModel {
        return ImageListViewModel.getInstance(this)
    }

    override fun getDetailsVModel(): IDetailsViewModel {
        return DetailsViewModel.getInstance(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                val backEntry =
                    supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
                supportFragmentManager.popBackStack()
            }
        }
        return true
    }
}