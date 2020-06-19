package net.borysw.blitz

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.Disposable
import org.junit.jupiter.api.Test

internal class SafeDisposableTest {

    @Test
    fun dispose() {
        val disposable = mock<Disposable>()
        val testedObj = SafeDisposable()

        testedObj.set(disposable)
        testedObj.dispose()

        verify(disposable).dispose()
    }

    @Test
    fun autoDispose() {
        val disposable1 = mock<Disposable>()
        val disposable2 = mock<Disposable>()
        val testedObj = SafeDisposable()

        testedObj.set(disposable1)
        testedObj.set(disposable2)

        verify(disposable1).dispose()

        testedObj.dispose()

        verify(disposable2).dispose()
    }
}