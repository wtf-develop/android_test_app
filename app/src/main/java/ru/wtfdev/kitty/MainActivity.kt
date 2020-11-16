package ru.wtfdev.kitty

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._navigation.BaseActivty
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.utils.SharingShortcutsManager


/**
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 */
class MainActivity : BaseActivty() {
    override var startFragment: String = ImageListView::class.qualifiedName ?: ""

    private lateinit var sharingShortcutsManager: SharingShortcutsManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerComponent.create().inject(this)
        setSupportActionBar(my_toolbar)
        val sharingShortcutsManager = SharingShortcutsManager().also {
            it.removeAllDirectShareTargets(this)
            it.pushDirectShareTargets(this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.menu_item_add) {
            navigation.moveTo(AddLinkView::class.qualifiedName)
        }
        return true
    }
}