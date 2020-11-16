package ru.wtfdev.kitty.detail

import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.fragment_details.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseFragment


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

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()

        mDetector = GestureDetectorCompat(this.context, MyGestureListener(viewModel))
        touchHandler.setOnTouchListener { _, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
        }
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
        fun newInstance(vmodel: IDetailsViewModel) = DetailsView(vmodel)
    }

    private class MyGestureListener(val viewModel: IDetailsViewModel) :
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            return true
        }


        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (Math.abs(velocityX) * 3f < Math.abs(velocityY)) {
                viewModel.close()
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

}