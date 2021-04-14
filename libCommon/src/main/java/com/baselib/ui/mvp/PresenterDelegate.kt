package com.baselib.ui.mvp

import android.os.Bundle
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/28
 */
class PresenterDelegate<V : BaseMvpView> : AbsPresenter<V>() {
    var presenter: BaseMvpPresenter<V>? = object : AbsPresenter<V>(){}

    fun delegate(presenter: BaseMvpPresenter<V>){
        this.presenter = presenter
    }

    override fun attach(view: V?) {
        super.attach(view)
        presenter?.attach(view)
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        presenter?.restoreInstanceState(savedInstanceState)
    }

    override fun saveInstanceState(outState: Bundle?) {
        presenter?.saveInstanceState(outState)
    }

    override fun detach() {
        super.detach()
        presenter?.detach()
    }

    override fun detachOnPause() {
        super.detachOnPause()
        presenter?.detachOnPause()
    }

    override fun resume() {
        super.resume()
        presenter?.resume()
    }
}