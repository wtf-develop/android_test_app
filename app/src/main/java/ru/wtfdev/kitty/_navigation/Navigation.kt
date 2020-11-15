package ru.wtfdev.kitty._navigation

import androidx.fragment.app.FragmentManager
import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._models.ItemModel
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.add_link.AddLinkViewModel
import ru.wtfdev.kitty.detail.DetailsRepository
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.DetailsViewModel
import ru.wtfdev.kitty.list.ImageListView
import ru.wtfdev.kitty.list.ImageListViewModel
import ru.wtfdev.kitty.utils.BaseFragment
import ru.wtfdev.kitty.utils.IBaseFragment


//TODO Need to migrate this to Dagger later
//Create Fragment with ViewModel with Navigation
//Create Fragment with ViewModel with Navigation
//Create Fragment with ViewModel with Navigation


//common navigation interface
interface INavigation {
    fun toList()
    fun toDetails(obj: ItemModel)
    fun moveTo(tag: String, backstack: Boolean = true): IBaseFragment?
    fun getTitle(tag: String): Int
}

//interface for routing fragments
interface INaviJump {
    fun getNaviFragmentManager(): FragmentManager
}

//implementation for INavigation
class Navigation private constructor(val navigateAction: INaviJump) : INavigation {

    override fun toList() {
        moveTo(ImageListView::class.qualifiedName ?: "", false)
    }

    override fun toDetails(obj: ItemModel) {
        DetailsRepository.itemData = obj //need to replace it - too bad for MVVM
        moveTo(DetailsView::class.qualifiedName ?: "")
    }

    override fun moveTo(tag: String, backstack: Boolean): IBaseFragment? {
        var fragment: BaseFragment?
        when (tag) {
            ImageListView::class.qualifiedName -> fragment =
                ImageListView.newInstance(ImageListViewModel.getInstance(this))

            DetailsView::class.qualifiedName -> fragment =
                DetailsView.newInstance(DetailsViewModel.getInstance(this))

            AddLinkView::class.qualifiedName -> fragment =
                AddLinkView.newInstance(AddLinkViewModel.getInstance(this))

            else -> return null
        }
        var transaction = navigateAction.getNaviFragmentManager().beginTransaction()
        transaction.add(R.id.fragments, fragment, tag)
        if (backstack) {
            transaction.addToBackStack(tag)
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        transaction.commit()
        return fragment
    }


    override fun getTitle(tag: String): Int {
        when (tag) {
            ImageListView::class.qualifiedName -> return R.string.app_name
            DetailsView::class.qualifiedName -> return R.string.details
            AddLinkView::class.qualifiedName -> return R.string.add_link_view
        }
        return R.string.error
    }

    companion object {
        fun getInstance(myNavi: INaviJump): INavigation = Navigation(myNavi)
    }
}