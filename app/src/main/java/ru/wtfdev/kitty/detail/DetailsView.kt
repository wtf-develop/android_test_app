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
    private val BUNDLE_KEY = "JSON"
    private lateinit var external_data: String

    @Inject
    lateinit var viewModel: IDetailsViewModel


    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        external_data = ""
        arguments?.let {
            external_data = it.getString(BUNDLE_KEY) ?: ""
        }
        viewModel.setDataString(external_data)
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(BUNDLE_KEY, external_data)
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(bundle: Bundle?): DetailsView {
            return DetailsView().apply {
                arguments = bundle
            }
        }
    }

}