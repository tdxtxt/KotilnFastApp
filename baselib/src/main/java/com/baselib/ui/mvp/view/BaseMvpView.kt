package com.baselib.ui.mvp.view

import android.app.Activity
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.mvp.view.IView

/**
 * 所有View层接口的基类
 */
interface BaseMvpView : IView {
    fun <T : Activity> getActivityNew(): T?
}