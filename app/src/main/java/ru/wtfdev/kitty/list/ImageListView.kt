package ru.wtfdev.kitty.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._navigation.implementation.BaseFragment
import ru.wtfdev.kitty.databinding.FragmentImageListBinding
import ru.wtfdev.kitty.list.implementation.adapter.ListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class ImageListView : BaseFragment() {
    private var scroll = 0

    @Inject
    lateinit var viewModel: IImageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = viewManager
        binding.recyclerView.adapter = viewAdapter
        binding.loader.visibility = View.VISIBLE
        binding.pull2refresh.visibility = View.GONE
        binding.pull2refresh.setOnRefreshListener {
            viewModel.updateList(true)
        }
        binding.pull2refresh.setColorSchemeResources(
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
                binding.recyclerView.scrollToPosition(scroll)
            }
            binding.pull2refresh.isRefreshing = false
            hideError()
            binding.pull2refresh.visibility = View.VISIBLE
            binding.loader.visibility = View.GONE
            (binding.recyclerView.adapter as? ListAdapter)?.setData(it)
        }
        viewModel.subscribeOnError {
            binding.pull2refresh.isRefreshing = false
            showError(it) {
                binding.loader.visibility = View.GONE
                binding.pull2refresh.visibility = View.VISIBLE
                binding.pull2refresh.isRefreshing = true
                viewModel.updateList(true)
            }
            binding.loader.visibility = View.GONE
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("scroll", viewManager?.findFirstVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ImageListView()
    }
}