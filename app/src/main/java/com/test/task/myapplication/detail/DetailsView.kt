package com.test.task.myapplication.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.task.myapplication.INavigation
import com.test.task.myapplication.R
import com.test.task.myapplication.utils.AutoDisposable
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsView : Fragment() {
    private val IMAGE_ID = "imageid"
    private var image_id: String? = null
    lateinit var viewModel: IDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image_id = it.getString(IMAGE_ID)
        }
        viewModel = (activity as INavigation).getDetailsVModel()
        viewModel.setLifecycle(AutoDisposable().bindTo(this.lifecycle))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribeOnChange {
            detail_title.text = it.title
            detail_desc.text = it.id
            viewModel.loadImageTo(detail_image, it.imageUrl)
        }
        viewModel.update()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            DetailsView().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_ID, param1)
                }
            }
    }
}