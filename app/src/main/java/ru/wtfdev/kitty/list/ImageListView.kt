package ru.wtfdev.kitty.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_image_list.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._navigation.implementation.BaseFragment
import ru.wtfdev.kitty.list.adapter.ListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class ImageListView : BaseFragment() {
    private var scroll = 0

    @Inject
    lateinit var viewModel: IImageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    var viewManager: LinearLayoutManager? = null
    private var scrollList = true

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
        pull2refresh.setOnRefreshListener {
            viewModel.updateList(true)
        }
        pull2refresh.setColorSchemeResources(
            R.color.base_main,
            R.color.base_secondary1,
            R.color.base_secondary2
        )
        viewModel.updateList()
    }


    override fun onDataBing() {
        viewModel.subscribeOnChange {
            if (scrollList) {
                scrollList = false
                recycler_view.scrollToPosition(scroll)
            }
            pull2refresh.isRefreshing = false
            hideError()
            pull2refresh.visibility = View.VISIBLE
            loader.visibility = View.GONE
            (recycler_view.adapter as? ListAdapter)?.setData(it)
        }
        viewModel.subscribeOnError {
            pull2refresh.isRefreshing = false
            showError(it) {
                loader.visibility = View.GONE
                pull2refresh.visibility = View.VISIBLE
                pull2refresh.isRefreshing = true
                viewModel.updateList(true)
            }
            loader.visibility = View.GONE
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("scroll", viewManager?.findFirstVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance() = ImageListView()
    }
}