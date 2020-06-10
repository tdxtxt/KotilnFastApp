package com.baselib.ui.mvp.presenter

import android.os.Bundle
import com.baselib.ui.mvp.BaseMvpView

/**
 * @date 2017/11/17
 * @description 所有Presenter的基类，并不强制实现这些方法，有需要在重写
 */
open class BaseMvpPresenter<V : BaseMvpView> {
    /**
     * V层view
     */
    private var mView: V? = null

    /**
     * Presenter被创建后调用
     */
    fun onCreatePersenter(savedState: Bundle?){

    }

    /**
     * 绑定view
     */
    fun onAttachMvpView(mvpView: V){
        mView = mvpView
    }

    /**
     * View是否绑定
     */
    fun isViewAttached(): Boolean {
        return mView != null
    }

    /**
     * 解除绑定View
     */
    fun onDetachMvpView(){
        mView = null
    }

    /**
     * Presenter被销毁时调用
     */
    fun onDestroyPersenter() {

    }

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中的onSaveInstanceState
     * 时机相同
     *
     * @param outState
     */
    fun onSaveInstanceState(outState: Bundle) {

    }

    /**
     * 获取V层接口View
     */
    fun getMvpView(): V? {
        return mView
    }

}