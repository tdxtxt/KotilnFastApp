package com.baselib.ui.mvp.view.fragment

import android.app.Activity
import android.os.Bundle
import com.baselib.ui.fragment.BaseFragment
import com.baselib.ui.mvp.PresenterDelegate
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/14
 */
open abstract class BaseMvpFragment : BaseFragment(), BaseMvpView {
    private val mvpDelegate: PresenterDelegate<BaseMvpView> = PresenterDelegate()
    abstract fun <V : BaseMvpView> createPresenter(): BaseMvpPresenter<V>?
    abstract fun createMvpView(): BaseMvpView?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDelegate.delegate(createPresenter())
        mvpDelegate.attach(createMvpView())
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.resume()
    }

    override fun onPause() {
        super.onPause()
        mvpDelegate.detachOnPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvpDelegate.saveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.detach()
    }

    override fun <T : Activity> getActivityNew(): T? {
        return activity as T?
    }
}