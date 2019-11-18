package com.baselib.ui.activity

import android.app.Dialog
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.Unbinder
import com.baselib.ui.mvp.view.IView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
abstract class BaseActivity : RxAppCompatActivity(),IView {
    private lateinit var mProgressDialog: Dialog
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId())
        unbinder = ButterKnife.bind(this)
        initUi()

    }

    abstract fun getLayoutResId(): Int
    fun initUi(){}

    override fun showProgressBar() {
    }

}