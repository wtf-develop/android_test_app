package ru.wtfdev.kitty

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.detail.IDetailsViewModel
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.BaseFragment
import ru.wtfdev.kitty.utils.IBaseFragment


/**
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 */
//Navigation interface
//Navigation interface
//Navigation interface
interface INavigation {
    fun toList()
    fun toDetails()
    fun getImageListVModel(): IImageListViewModel
    fun getDetailsVModel(): IDetailsViewModel
}


//Navigation implementation
//Navigation implementation
//Navigation implementation
class MainActivity : AppCompatActivity(), INavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        if (savedInstanceState == null) {
            moveTo(ImageListView.tag, false).apply {
                this?.setIsForegroung(true)
            }
        }


        supportFragmentManager.addOnBackStackChangedListener {
            val count = supportFragmentManager.backStackEntryCount
            var tag = ""
            var fragment: BaseFragment? = null
            val mainFragment =
                supportFragmentManager.findFragmentByTag(ImageListView.tag) as? BaseFragment
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
                tag = ImageListView.tag
                fragment = mainFragment
            }
            fragment?.setIsForegroung(true)
            fragment?.onSubscribeBindings()
            title = getString(getTitle(tag))
            supportActionBar?.setDisplayHomeAsUpEnabled(getBackButton())
        }
    }

    override fun toList() {
        moveTo(ImageListView.tag, false)
    }

    override fun toDetails() {
        moveTo(DetailsView.tag)
    }


    private fun moveTo(tag: String, backstack: Boolean = true): IBaseFragment? {
        var fragment: BaseFragment?
        when (tag.toLowerCase()) {
            ImageListView.tag -> fragment = ImageListView.newInstance()
            DetailsView.tag -> fragment = DetailsView.newInstance("id")
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

    private fun getTitle(tag: String): Int {
        when (tag.toLowerCase()) {
            ImageListView.tag -> return R.string.app_name
            DetailsView.tag -> return R.string.details
        }
        return R.string.error
    }

    private fun getBackButton(): Boolean {
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            }
        } else if (id == R.id.menu_item_add) {
            Toast.makeText(this, "Soon", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}