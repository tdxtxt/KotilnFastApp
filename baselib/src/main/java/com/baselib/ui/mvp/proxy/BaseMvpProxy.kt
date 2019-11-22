package com.baselib.ui.mvp.proxy

import android.os.Bundle
import com.baselib.ui.mvp.BaseMvpView
import com.baselib.ui.mvp.factory.PresenterMvpFactory
import com.baselib.ui.mvp.presenter.BaseMvpPresenter

/**
 * 代理实现类，用来管理Presenter的生命周期，还有和view之间的关联
 */
class BaseMvpProxy<V : BaseMvpView, P : BaseMvpPresenter<V>> constructor(var mFactory: PresenterMvpFactory<V, P>) : PresenterProxyInterface<V, P> {
    /**
     * 获取onSaveInstanceState中bundle的key
     */
    private val PRESENTER_KEY = "presenter_key"

    /**
     * Presenter工厂类
     */
    private var mPresenter: P? = null
    private var mBundle: Bundle? = null
    private var mIsAttchView: Boolean = false

    /**
     * 设置Presenter的工厂类,这个方法只能在创建Presenter之前调用,也就是调用getMvpPresenter()之前，如果Presenter已经创建则不能再修改
     */
    override fun setPresenterFactory(presenterFactory: PresenterMvpFactory<V, P>) {
        mFactory = presenterFactory
    }
    /**
     * 获取Presenter的工厂类
     */
    override fun getPresenterFactory(): PresenterMvpFactory<V, P> = mFactory
    /**
     * 获取创建的Presenter
     *
     * @return 指定类型的Presenter
     * 如果之前创建过，而且是以外销毁则从Bundle中恢复
     */
    override fun getMvpPresenter(): P? {
        if(mPresenter == null){
            mPresenter = mFactory.createMvpPresenter()
            mPresenter?.onCreatePersenter(mBundle?.getBundle(PRESENTER_KEY)?: null)
        }
        return mPresenter
    }

    fun onCreate(mvpView : V){
        getMvpPresenter()
        if(!mIsAttchView){
            mPresenter?.onAttachMvpView(mvpView)
            mIsAttchView = true
        }
    }

    fun onResume() = getMvpPresenter()
    /**
     * 销毁Presenter持有的View
     */
    private fun onDetachMvpView(){
        if(mIsAttchView){
            mPresenter?.onDetachMvpView()
            mIsAttchView = false
        }
    }
    /**
     * 销毁Presenter
     */
    fun onDestroy(){
        onDetachMvpView()
        mPresenter?.onDestroyPersenter()
        mPresenter = null
    }
    /**
     * 意外销毁的时候调用
     * @return Bundle，存入回调给Presenter的Bundle和当前Presenter的id
     */
    fun onSaveInstanceState(): Bundle{
        getMvpPresenter()
        return Bundle().apply {
            putBundle(PRESENTER_KEY,Bundle().apply {
                mPresenter?.onSaveInstanceState(this)
            })
        }
    }
    /**
     * 意外关闭恢复Presenter
     * @param savedInstanceState 意外关闭时存储的Bundler
     */
    fun onRestoreInstanceState(savedInstanceState: Bundle?){
        mBundle = savedInstanceState
    }
}