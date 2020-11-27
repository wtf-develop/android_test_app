package ru.wtfdev.kitty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.wtfdev.kitty._navigation.implementation.BaseActivty
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.databinding.ActivityMainBinding
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.utils.SharingShortcutsManager


/**
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 */
class MainActivity : BaseActivty() {
    override var startFragment: String = "/list"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
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
            navigation.push("/add")
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