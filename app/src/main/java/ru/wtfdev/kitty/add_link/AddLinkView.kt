package ru.wtfdev.kitty.add_link

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.wtfdev.kitty._navigation.implementation.BaseFragment
import ru.wtfdev.kitty.add_link.implementation.AddLinkViewModel
import ru.wtfdev.kitty.databinding.FragmentAddLinkBinding
import ru.wtfdev.kitty.utils.CloseGestureListener
import javax.inject.Inject


@AndroidEntryPoint
class AddLinkView : BaseFragment() {

    //@Inject
    private val viewModel: AddLinkViewModel by viewModels()

    override fun onDataBing() {
        viewModel.subscribeOnChange {
            if (it.state) {
                hideError()
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.subscribeOnError {
            showError(it)
        }
        viewModel.subscribeOnImageSuccess {
            if (it) showKeyboard()
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
        hideKeyboard()
    }

    private var _binding: FragmentAddLinkBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    val handler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        val url = viewModel.getUrlFlow(
            activity?.intent,
            savedInstanceState,
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        )
        viewModel.setLifeCycle(viewLifecycleOwner)
        binding.linkUrl.setText(url)
        if (url.isNotEmpty()) {
            viewModel.loadImageTo(binding.imageUrl, url)
        }
        binding.buttonUrl.setOnClickListener {
            viewModel.saveLoadedImageUrl(binding.linkTitle.text.toString())
        }
        binding.closeFragment.setOnClickListener {
            viewModel.close()
        }
        binding.linkUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(Runnable {
                    viewModel.loadImageTo(
                        binding.imageUrl,
                        viewModel.extractUrl(binding.linkUrl.text.toString())
                    )
                }, 1500)
                hideError()
            }

        })

        val submitListener = OnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_SEND, EditorInfo.IME_ACTION_GO, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.saveLoadedImageUrl(binding.linkTitle.text.toString())
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
            }
            false
        }
        binding.linkUrl.setOnEditorActionListener(submitListener)
        binding.linkTitle.setOnEditorActionListener(submitListener)
        val mDetector =
            GestureDetectorCompat(this.context, CloseGestureListener { viewModel.close() })
        binding.parentLay.setOnTouchListener { _, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
        }
    }

    private fun showKeyboard() {
        if (binding.linkTitle.hasFocus()) return
        try {
            binding.linkTitle.requestFocus()
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            binding.linkTitle.requestFocus()
        } catch (e: Exception) {
        }
    }

    private fun hideKeyboard() {
        binding.parentLay.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.parentLay.windowToken, 0)
        binding.parentLay.requestFocus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveUrlState(outState, binding.linkUrl.text.toString())
        super.onSaveInstanceState(outState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = AddLinkView()
    }
}