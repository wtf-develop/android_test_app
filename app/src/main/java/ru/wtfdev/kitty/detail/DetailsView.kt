package ru.wtfdev.kitty.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.fragment_details.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty._navigation.BaseFragment
import ru.wtfdev.kitty.utils.CloseGestureListener


class DetailsView private constructor(val viewModel: IDetailsViewModel) : BaseFragment() {
    private val IMAGE_ID = "imageid"
    private var image_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComponent.create().inject(this)
        arguments?.let {
            image_id = it.getString(IMAGE_ID)
        }
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

        val mDetector =
            GestureDetectorCompat(this.context, CloseGestureListener { viewModel.close() })
        touchHandler.setOnTouchListener { _, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
        }
    }

    override fun onDataBing() {
        viewModel.subscribeOnChange {
            detail_title.text = it.title
            viewModel.loadImageTo(detail_image, it.link)
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
    }

    companion object {
        fun newInstance(vmodel: IDetailsViewModel) = DetailsView(vmodel)
    }

}