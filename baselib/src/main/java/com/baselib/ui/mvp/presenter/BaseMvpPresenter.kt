package com.baselib.ui.mvp.presenter

import android.os.Bundle
import com.baselib.ui.mvp.view.BaseMvpView

/**
 * @date 2017/11/17
 * @description 所有Presenter的基类，并不强制实现这些方法，有需要在重写
 */
interface BaseMvpPresenter<V : BaseMvpView> {
    fun attach(view: V?)

    fun restoreInstanceState(savedInstanceState: Bundle?)

    fun saveInstanceState(outState: Bundle?)

    fun resume()

    fun detach()

    fun detachOnPause()
}