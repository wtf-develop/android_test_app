package ru.wtfdev.kitty.add_link

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
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
import kotlinx.android.synthetic.main.fragment_add_link.*
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseFragment

class AddLinkView private constructor(val viewModel: IAddLinkViewModel) : BaseFragment() {

    private val URL_KEY = "img_url"
    override fun onDataBing() {
        viewModel.subscribeOnChange {
            if (it) hideError()
        }
        viewModel.subscribeOnError {
            showError(it) {
                viewModel.save(linkUrl.text.toString())
            }
        }
    }

    override fun onDataUnBing() {
        viewModel.unsubscribeAll()
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
        val urlIntent = viewModel.getUrlFromIntent(activity?.intent)
        var url = savedInstanceState?.getString(URL_KEY, urlIntent) ?: urlIntent
        if(url.isEmpty()){//clipboard
            var clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            clipboard?.let { clip->
                if(clip.hasPrimaryClip()&&(true==clip.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN))){
                    val item = clip.primaryClip?.getItemAt(0)
                    url = viewModel.extractUrl(item?.text?.toString())
                }
            }
        }
        linkUrl.setText(viewModel.extractUrl(url))
        if (url.isNotEmpty()) {
            viewModel.loadImageTo(imageUrl, url)
        }
        buttonUrl.setOnClickListener {
            viewModel.save(viewModel.extractUrl(linkUrl.text.toString()))
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
                }, 1000)
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(URL_KEY, linkUrl.text.toString())
        super.onSaveInstanceState(outState)
    }


    companion object {
        fun newInstance(vmodel: IAddLinkViewModel) = AddLinkView(vmodel)
    }
}