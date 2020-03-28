package net.borysw.blitz.app

import io.reactivex.rxjava3.disposables.Disposable

class SafeDisposable : Disposable {
    private var disposable: Disposable? = null

    fun set(disposable: Disposable) {
        this.disposable?.dispose()
        this.disposable = disposable
    }

    override fun isDisposed(): Boolean = disposable?.isDisposed ?: false

    override fun dispose() {
        disposable?.dispose()
    }
}