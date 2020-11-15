package ru.wtfdev.kitty.add_link

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._dagger.DaggerComponent
import ru.wtfdev.kitty.utils.BaseFragment

class AddLinkView private constructor(val viewModel: IAddLinkViewModel) : BaseFragment() {

    override fun onDataBing() {

    }

    override fun onDataUnBing() {

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
        val url = activity?.intent?.extras?.getString("url") ?: ""
    }

    companion object {
        fun newInstance(vmodel: IAddLinkViewModel) = AddLinkView(vmodel)
    }
}