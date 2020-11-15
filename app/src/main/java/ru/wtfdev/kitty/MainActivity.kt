package ru.wtfdev.kitty

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.utils.BaseActivty


/**
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 * we use Activity only as Navigation object. There is no UI elements except toolbar
 */
class MainActivity : BaseActivty() {
    override var startFragment: String = ImageListView::class.qualifiedName ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComponent.create().inject(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
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