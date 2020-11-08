package com.test.task.myapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.test.task.myapplication.detail.DetailsView
import com.test.task.myapplication.detail.DetailsViewModel
import com.test.task.myapplication.detail.IDetailsViewModel
import com.test.task.myapplication.list.IImageListViewModel
import com.test.task.myapplication.list.ImageListView
import com.test.task.myapplication.list.ImageListViewModel


interface INavigation {
    fun toList()
    fun toDetails()
    fun getImageListVModel(): IImageListViewModel
    fun getDetailsVModel(): IDetailsViewModel
}

class MainActivity : AppCompatActivity(), INavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            moveTo("startpage", false)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val count = supportFragmentManager.backStackEntryCount
            var tag = "startpage"
            if (count > 0) {
                val backEntry = supportFragmentManager.getBackStackEntryAt(count - 1)
                tag = backEntry.name ?: tag
            }
            title = getTitle(tag)
            supportActionBar?.setDisplayHomeAsUpEnabled(getBackButton(tag))
        }
    }

    override fun toList() {
        moveTo("startpage", false)
    }

    override fun toDetails() {
        moveTo("detailspage")
    }


    private fun moveTo(tag: String, backstack: Boolean = true) {
        var fragment: Fragment? = null
        when (tag.toLowerCase()) {
            "startpage" -> fragment = ImageListView.newInstance()
            "detailspage" -> fragment = DetailsView.newInstance("id")
            else -> return
        }
        fragment?.let {
            var transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragments, it, tag)
            if (backstack) {
                transaction.addToBackStack(tag)
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            transaction.commit()
        }
    }

    private fun getTitle(tag: String): String {
        when (tag.toLowerCase()) {
            "startpage" -> return "Kitty app"
            "detailspage" -> return "Details about kitty"
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
                supportFragmentManager.popBackStack()
            }
        }
        return true
    }
}