package com.baselib.ui.mvp.view.activity

import android.os.Bundle
import android.util.Log
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.mvp.BaseMvpView
import com.baselib.ui.mvp.factory.PresenterMvpFactory
import com.baselib.ui.mvp.factory.PresenterMvpFactoryImpl
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.proxy.BaseMvpProxy
import com.baselib.ui.mvp.proxy.PresenterProxyInterface

/**
 * @作者： ton
 * @时间： 2018\5\11 0011
 * @描述： 创建Presenter有两种方式：1、重写createPresenter方法；2、class类上面添加注释@CreatePresenter(XXXPresenter.class)
 * @传入参数说明：
 * @返回参数说明：
 */
open abstract class BaseMvpActivity<V : BaseMvpView, P : BaseMvpPresenter<V>> : BaseActivity(), PresenterProxyInterface<V, P> {
    private val PRESENTER_SAVE_KEY = "presenter_save_key"

    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private var mProxy: BaseMvpProxy<V, P> = BaseMvpProxy<V, P>(PresenterMvpFactoryImpl.createFactory(javaClass, createPresenter()))

    abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("perfect-mvp", "V onCreate")
        Log.e("perfect-mvp", "V onCreate mProxy = $mProxy")
        Log.e("perfect-mvp", "V onCreate this = " + this.hashCode())

        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY)!!)
        }
        mProxy.onCreate(this as V)
    }

    override fun onResume() {
        super.onResume()
        Log.e("perfect-mvp", "V onResume")
        mProxy.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.e("perfect-mvp", "V onSaveInstanceState")
        outState?.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState())
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("perfect-mvp", "V onDestroy = ")
        mProxy.onDestroy()
    }

    override fun setPresenterFactory(presenterFactory: PresenterMvpFactory<V, P>) {
        Log.e("perfect-mvp", "V setPresenterFactory")
        mProxy.setPresenterFactory(presenterFactory)
    }

    override fun getPresenterFactory(): PresenterMvpFactory<V, P> {
        Log.e("perfect-mvp", "V getPresenterFactory")
        return mProxy.getPresenterFactory()
    }

    override fun getMvpPresenter(): P? {
        Log.e("perfect-mvp", "V getMvpPresenter")
        return mProxy.getMvpPresenter()
    }
}