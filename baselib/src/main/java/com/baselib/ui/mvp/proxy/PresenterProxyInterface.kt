package com.baselib.ui.mvp.proxy

import com.baselib.ui.mvp.BaseMvpView
import com.baselib.ui.mvp.factory.PresenterMvpFactory
import com.baselib.ui.mvp.presenter.BaseMvpPresenter

/**
 * 代理Presenter接口
 */
interface PresenterProxyInterface<V : BaseMvpView, P : BaseMvpPresenter<V>> {

    /**
     * 设置创建Presenter的工厂
     * @param presenterFactory PresenterFactory类型
     */
    fun setPresenterFactory(presenterFactory: PresenterMvpFactory<V, P>)

    /**
     * 获取Presenter的工厂类
     * @return 返回PresenterMvpFactory类型
     */
    fun getPresenterFactory(): PresenterMvpFactory<V, P>


    /**
     * 获取创建的Presenter
     * @return 指定类型的Presenter
     */
    fun getMvpPresenter(): P?
}