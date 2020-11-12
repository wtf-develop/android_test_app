package ru.wtfdev.kitty.utils

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class AutoDisposable {
    var compositeDisposable: CompositeDisposable

    constructor() {
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun disconnectAllListeners() {
        compositeDisposable.clear()
    }
}