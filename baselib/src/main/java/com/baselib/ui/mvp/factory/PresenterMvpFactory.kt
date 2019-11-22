package com.baselib.ui.mvp.factory

import com.baselib.ui.mvp.BaseMvpView
import com.baselib.ui.mvp.presenter.BaseMvpPresenter

interface PresenterMvpFactory<V : BaseMvpView, P : BaseMvpPresenter<V>> {
    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    fun createMvpPresenter(): P
}