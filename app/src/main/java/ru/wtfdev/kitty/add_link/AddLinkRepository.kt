package ru.wtfdev.kitty.add_link

import ru.wtfdev.kitty._dagger.DaggerComponent


interface IAddLinkRepository


class AddLinkRepository : IAddLinkRepository {
    init {
        DaggerComponent.create().inject(this)
    }


    companion object {
        fun getInstance(): IAddLinkRepository {
            return AddLinkRepository()
        }
    }
}