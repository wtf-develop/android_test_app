package ru.wtfdev.kitty.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import dagger.hilt.android.AndroidEntryPoint

import ru.wtfdev.kitty._navigation.implementation.BaseFragment
import ru.wtfdev.kitty.databinding.FragmentDetailsBinding
import ru.wtfdev.kitty.utils.CloseGestureListener
import javax.inject.Inject


@AndroidEntryPoint
class DetailsView : BaseFragment() {
    private val IMAGE_ID = "imageid"
    private var image_id: String? = null


    @Inject
    lateinit var viewModel: IDetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image_id = it.getString(IMAGE_ID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()

        val mDetector =
            GestureDetectorCompat(this.context, CloseGestureListener { viewModel.close() })
        binding.touchHandler.setOnTouchListener { _, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
        }
    }

    override fun onDataBing() {
        viewModel.subscribeOnChange {
            binding.detailTitle.text = it.title
            viewModel.loadImageTo(binding.detailImage, it.link)
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = DetailsView()
    }

}