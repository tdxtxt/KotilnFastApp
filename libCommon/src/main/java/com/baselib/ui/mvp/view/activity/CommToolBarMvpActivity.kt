package com.baselib.ui.mvp.view.activity

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import com.baselib.ui.activity.CommToolBarActivity
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.PresenterDelegate
import com.baselib.ui.mvp.presenter.BaseMvpPresenter

/**
 * @作者： ton
 * @时间： 2018\4\28 0028
 * @描述： 创建Presenter有两种方式：1、重写createPresenter方法；2、class类上面添加注释@CreatePresenter(XXXPresenter.class)
 * @传入参数说明：
 * @返回参数说明：
 */
open abstract class CommToolBarMvpActivity : CommToolBarActivity(), BaseMvpView {
    private val mvpDelegate: PresenterDelegate<BaseMvpView> = PresenterDelegate()
    abstract fun createPresenter(): BaseMvpPresenter<*>
    abstract fun createMvpView(): BaseMvpView?

    override fun onCreate(savedInstanceState: Bundle?) {
        mvpDelegate.delegate(createPresenter() as BaseMvpPresenter<BaseMvpView>)
        mvpDelegate.attach(createMvpView())
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.resume()
    }

    override fun onPause() {
        super.onPause()
        mvpDelegate.detachOnPause()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        mvpDelegate.saveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.detach()
    }
}