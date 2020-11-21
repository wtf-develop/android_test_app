package ru.wtfdev.kitty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ru.wtfdev.kitty._navigation.implementation.BaseActivty
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
        setSupportActionBar(my_toolbar)
        SharingShortcutsManager().also {
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
        } else if (id == R.id.menu_sources) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/wtf-develop/android_test_app")
            try {
                startActivity(intent)
            } catch (_: Exception) {
            }
        } else if (id == R.id.menu_privacy) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://docs.google.com/document/d/e/2PACX-1vRNNUb1h-Zw4FiKzvy0vNnWDv2cRU0KtyWzC-1_XrcIj-BuNce0ppXj7PAgFr7xEOqPYv7yoZSC4RjI/pub")
            try {
                startActivity(intent)
            } catch (_: Exception) {
            }

        }
        return true
    }
}