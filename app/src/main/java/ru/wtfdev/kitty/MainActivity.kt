package ru.wtfdev.kitty

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.detail.IDetailsViewModel
import ru.wtfdev.kitty.list.IImageListViewModel
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.BaseActivty


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
class MainActivity : BaseActivty(), INavigation {
    override var startFragment: String
        get() = ImageListView::class.simpleName ?: ""
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
    }

    override fun toList() {
        moveTo(ImageListView::class.simpleName ?: "", false)
    }

    override fun toDetails() {
        moveTo(DetailsView::class.simpleName ?: "")
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
        super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.menu_item_add) {
            Toast.makeText(this, "Soon", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}