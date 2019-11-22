package com.baselib.ui.mvp.factory

import com.baselib.ui.mvp.BaseMvpView
import com.baselib.ui.mvp.presenter.BaseMvpPresenter

/**
 * Presenter工厂实现类
 */
class PresenterMvpFactoryImpl<V : BaseMvpView, P : BaseMvpPresenter<V>> constructor(mPresenter: P?, presenterClass: Class<P>?) : PresenterMvpFactory<V, P> {

    private val mPresenterClass:  Class<P>? = null
    private var mPresenter: P? = null

    constructor(mPresenter: P?): this(mPresenter, null)

    constructor(presenterClass: Class<P>?): this(null, presenterClass)

    override fun createMvpPresenter() = mPresenter?: mPresenterClass?.newInstance()!!

    companion object {
        fun <V : BaseMvpView, P : BaseMvpPresenter<V>> createFactory(viewClazz: Class<*>, presenter: P? = null): PresenterMvpFactoryImpl<V, P> {
            val annotation = viewClazz.getAnnotation(CreatePresenter::class.java)
            var aClass: Class<P>? = null
            if (annotation != null) {
                aClass = annotation.value as Class<P>
            }
            return aClass?.let { PresenterMvpFactoryImpl(it) } ?: PresenterMvpFactoryImpl(presenter)
        }
    }

}