package com.baselib.ui.mvp.presenter

import android.os.Bundle
import com.baselib.ui.mvp.view.BaseMvpView

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/28
 */
abstract class AbsPresenter <V : BaseMvpView> : BaseMvpPresenter<V>{
    protected var baseView: V? = null

    override fun attach(view: V?) {
        baseView = view
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {}

    override fun saveInstanceState(outState: Bundle?) {}

    override fun detach() {}

    override fun detachOnPause() {}

    override fun resume() {
    }
}