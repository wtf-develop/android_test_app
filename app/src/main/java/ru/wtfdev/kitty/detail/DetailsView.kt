package ru.wtfdev.kitty.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_details.*
import ru.wtfdev.kitty.INavigation
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseFragment


class DetailsView : BaseFragment() {
    private val IMAGE_ID = "imageid"
    private var image_id: String? = null
    lateinit var viewModel: IDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComponent.create().inject(this)
        arguments?.let {
            image_id = it.getString(IMAGE_ID)
        }
        viewModel = (activity as INavigation).getDetailsVModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()
    }

    override fun onDataBing() {
        viewModel.subscribeOnChange {
            detail_title.text = it.title
            detail_desc.text = it.id
            viewModel.loadImageTo(detail_image, it.imageUrl)
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
    }

    companion object {
        val tag = "detailspage"

        @JvmStatic
        fun newInstance(param1: String) =
            DetailsView().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_ID, param1)
                }
            }
    }
}