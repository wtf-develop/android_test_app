package ru.wtfdev.kitty._navigation.implementation

import ru.wtfdev.kitty.R
import ru.wtfdev.kitty._models.data.ItemModel
import ru.wtfdev.kitty._navigation.IBaseFragment
import ru.wtfdev.kitty._navigation.INavigation
import ru.wtfdev.kitty._navigation.INavigationProvider
import ru.wtfdev.kitty.add_link.AddLinkView
import ru.wtfdev.kitty.detail.DetailsView
import ru.wtfdev.kitty.detail.implementation.DetailsRepository
import ru.wtfdev.kitty.list.ImageListView


//implementation for INavigation
class Navigation(val navigateAction: INavigationProvider) : INavigation {

    override fun toList() {
        moveTo(ImageListView::class.qualifiedName ?: "", false)
    }

    override fun toDetails(obj: ItemModel) {
        DetailsRepository.itemData = obj //need to replace it - too bad for MVVM
        moveTo(DetailsView::class.qualifiedName)
    }

    override fun moveTo(tag: String?, backstack: Boolean): IBaseFragment? {
        if (tag == null) return null
        var fragment: BaseFragment?
        when (tag) {
            ImageListView::class.qualifiedName -> fragment =
                ImageListView.newInstance()

            DetailsView::class.qualifiedName -> fragment =
                DetailsView.newInstance()

            AddLinkView::class.qualifiedName -> fragment =
                AddLinkView.newInstance()

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

    override fun popBackStack() {
        if (navigateAction.getNaviFragmentManager().backStackEntryCount == 0) {
            navigateAction.finish()
        } else {
            navigateAction.getNaviFragmentManager().popBackStack()
        }
    }


    override fun getTitle(tag: String): Int {
        when (tag) {
            ImageListView::class.qualifiedName -> return R.string.main_title
            DetailsView::class.qualifiedName -> return R.string.details
            AddLinkView::class.qualifiedName -> return R.string.add_link_view
        }
        return R.string.error
    }
}