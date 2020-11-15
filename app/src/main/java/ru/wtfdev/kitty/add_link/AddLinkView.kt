package ru.wtfdev.kitty.add_link

import android.os.Bundle
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlIntent = viewModel.getUrlFromIntent(activity?.intent)
        val url = savedInstanceState?.getString(URL_KEY, urlIntent) ?: urlIntent
        linkUrl.setText(url)
        if (url.isNotEmpty()) {
            viewModel.loadImageTo(imageUrl, url)
        }
        buttonUrl.setOnClickListener {
            viewModel.save(linkUrl.text.toString())
        }
        close_fragment.setOnClickListener {
            viewModel.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("img_url", linkUrl.text.toString())
        super.onSaveInstanceState(outState)
    }


    companion object {
        fun newInstance(vmodel: IAddLinkViewModel) = AddLinkView(vmodel)
    }
}