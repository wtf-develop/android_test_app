package com.test.task.myapplication.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.task.myapplication.INavigation
import com.test.task.myapplication.R
import com.test.task.myapplication.list.adapter.ListAdapter
import com.test.task.myapplication.utils.AutoDisposable
import kotlinx.android.synthetic.main.fragment_image_list.*

class ImageListView : Fragment() {
    lateinit var viewModel: IImageListViewModel
    private var scroll = 0

    fun setVModel(vModel: IImageListViewModel): ImageListView {
        viewModel = vModel
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as INavigation).getImageListVModel()
        viewModel.setLifecycle(AutoDisposable().bindTo(this.lifecycle))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    var viewManager: LinearLayoutManager? = null
    private var scrollList = true

    private lateinit var snackbar: Snackbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scroll = savedInstanceState?.getInt("scroll", 0) ?: 0
        val orientation = this.resources.configuration.orientation
        val isTablet = resources.getBoolean(R.bool.is_tablet)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isTablet) {
                viewManager = GridLayoutManager(activity, 2)
            } else {
                viewManager = LinearLayoutManager(activity)
            }
        } else {
            if (isTablet) {
                viewManager = GridLayoutManager(activity, 3)
            } else {
                viewManager = GridLayoutManager(activity, 2)
            }
        }
        var viewAdapter = ListAdapter(viewModel)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = viewManager
        recycler_view.adapter = viewAdapter
        loader.visibility = View.VISIBLE
        pull2refresh.visibility = View.GONE
        snackbar = Snackbar
            .make(view, "Network error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                loader.visibility = View.GONE
                pull2refresh.visibility = View.VISIBLE
                pull2refresh.isRefreshing = true
                viewModel.updateList()
            }
        viewModel.subscribeOnChange {
            if (scrollList) {
                scrollList = false
                recycler_view.scrollToPosition(scroll)
            }
            pull2refresh.isRefreshing = false
            snackbar.dismiss()
            pull2refresh.visibility = View.VISIBLE
            loader.visibility = View.GONE
        }
        viewModel.subscribeOnError {
            pull2refresh.isRefreshing = false
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            snackbar.show()
            loader.visibility = View.GONE
        }

        pull2refresh.setOnRefreshListener {
            viewModel.updateList()
        }
        pull2refresh.setColorSchemeResources(
            android.R.color.holo_green_light,
            android.R.color.holo_red_light
        )

        viewModel.updateList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("scroll", viewManager?.findFirstVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageListView()
    }
}