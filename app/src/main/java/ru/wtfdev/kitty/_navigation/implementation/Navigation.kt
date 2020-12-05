package ru.wtfdev.kitty._navigation.implementation

import android.content.Context
import android.os.Bundle
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._navigation.IBaseFragment
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.INavigationProvider
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.list.ImageListView


//implementation for INavigation
class Navigation(private val navigateAction: INavigationProvider, private val ctx: Context) :
    INavigation {

    private var path = ""

    override fun getTitle(): String {
        val current = popPath().second
        when (current) {
            "list" -> return ctx.getString(R.string.main_title)
            "details" -> return ctx.getString(R.string.details)
            "add" -> return ctx.getString(R.string.add_link_view)
        }
        return ctx.getString(R.string.error)
    }


    override fun push(tag: String?, data: Bundle?, backstack: Boolean): IBaseFragment? {
        if (tag == null) return null
        var fragment: BaseFragment?
        when (tag) {
            "/list" -> fragment = ImageListView.newInstance()
            "/details" -> fragment = DetailsView.newInstance(data)
            "/add" -> fragment = AddLinkView.newInstance()
            else -> return null
        }


        var transaction = navigateAction.getNaviFragmentManager()?.beginTransaction()
        transaction?.add(R.id.fragments, fragment, tag)
        if (backstack) {
            transaction?.addToBackStack(tag)
            transaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            val pair = popPath()
            path = pair.first
        }
        path += tag
        transaction?.commit()
        return fragment
    }

    private fun popPath(): Pair<String, String> {
        var result = Pair(path, "")
        val arr = path.split("/")
        if (arr.isNotEmpty()) {
            result = Pair(arr.take(arr.size - 1).joinToString("/"), arr.takeLast(1)[0])
        }
        return result
    }

    override fun pop(auto: Boolean) {
        if (auto) {
            val pair = popPath()
            path = pair.first
        } else {
            if (navigateAction.getNaviFragmentManager()?.backStackEntryCount == 0) {
                navigateAction.finish()
            } else {
                navigateAction.getNaviFragmentManager()?.popBackStack()
            }
        }
    }

}