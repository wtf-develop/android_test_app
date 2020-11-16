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
import kotlinx.android.synthetic.main.fragment_add_link.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseFragment

class AddLinkView private constructor(val viewModel: IAddLinkViewModel) : BaseFragment() {


    override fun onDataBing() {
        viewModel.subscribeOnChange {
            if (it) hideError()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_link, container, false)
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
        linkUrl.setText(url)
        if (url.isNotEmpty()) {
            viewModel.loadImageTo(imageUrl, url)
        }
        buttonUrl.setOnClickListener {
            viewModel.saveLoadedImageUrl(linkTitle.text.toString())
        }
        close_fragment.setOnClickListener {
            viewModel.close()
        }
        linkUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(Runnable {
                    viewModel.loadImageTo(imageUrl, viewModel.extractUrl(linkUrl.text.toString()))
                }, 1500)
                hideError()
            }

        })

        val submitListener = OnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_SEND, EditorInfo.IME_ACTION_GO, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.saveLoadedImageUrl(linkTitle.text.toString())
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
            }
            false
        }
        linkUrl.setOnEditorActionListener(submitListener)
        linkTitle.setOnEditorActionListener(submitListener)

    }

    private fun showKeyboard() {
        if (linkTitle.hasFocus()) return
        try {
            linkTitle.requestFocus()
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            linkTitle.requestFocus()
        } catch (e: Exception) {
        }
    }

    private fun hideKeyboard() {
        parentLay.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(parentLay.windowToken, 0)
        parentLay.requestFocus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveUrlState(outState, linkUrl.text.toString())
        super.onSaveInstanceState(outState)
    }


    companion object {
        fun newInstance(vmodel: IAddLinkViewModel) = AddLinkView(vmodel)
    }
}