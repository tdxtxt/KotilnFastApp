package com.baselib.ui.activity

import android.os.Bundle
import android.support.annotation.ColorRes
import android.view.KeyEvent
import butterknife.ButterKnife
import butterknife.Unbinder
import com.baselib.R
import com.baselib.helper.DialogHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.dialog.NativeBaseDialog
import com.baselib.ui.dialog.child.NativeProgressDialog
import com.baselib.ui.mvp.view.IView
import com.gyf.immersionbar.ImmersionBar
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity



/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
abstract class BaseActivity : RxAppCompatActivity(),IView {
    private var mProgressDialog: NativeProgressDialog? = null
    private lateinit var unbinder: Unbinder
    private lateinit var stateLayout: StateLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initStatusBar()
        unbinder = ButterKnife.bind(this)
        stateLayout = initStateView()
        initUi()
        overridePendingTransition(R.anim.baselib_slide_in_form_right, 0)//进入的切换动画
    }

    /**
     * 多状态通用页面
     */
    open fun initStateView() = StateLayout(this)
            /*.config(loadingLayoutId = R.layout.custom_loading, //自定义加载中布局
                    errorLayoutId = R.layout.custom_error, //自定义加载失败布局
                    emptyLayoutId = R.layout.custom_empty, //自定义数据位为空的布局
                    useContentBgWhenLoading = true, //加载过程中是否使用内容的背景
                    enableLoadingShadow = true, //加载过程中是否启用半透明阴影盖在内容上面
                    retryAction = { //点击errorView的回调
                        ToastHelper.showToast("点击重试")
                    })*/
            .wrap(this)
            .showLoading()

    /**
     * 状态栏
     */
    open fun initStatusBar(){
        ImmersionBar.with(this)
                .transparentBar()//透明的状态栏
                .statusBarDarkFont(true)//false字体白色，true字体黑色
                .fullScreen(true)//全入侵
                .transparentNavigationBar()
                .init()
    }

    abstract fun getLayoutResId(): Int
    open fun initUi(){}

    override fun showProgressBar() {
        if (isFinishing) return
        if (mProgressDialog == null) mProgressDialog = DialogHelper.createProgressDialog(this, "请耐心等待，正在处理...", true)
        mProgressDialog?.setCancelableOnTouchOutside<NativeBaseDialog>(false)?.show<NativeBaseDialog>()
    }

    override fun hideProgressBar() {
        mProgressDialog?.hide()
    }

    override fun showLoadingView() {
        stateLayout.showLoading()
    }

    override fun showContentView() {
        stateLayout.showContent()
    }

    override fun showEmptyView() {
        stateLayout.showEmpty()
    }

    override fun showCustomView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorView(e: Throwable) {
        stateLayout.showEmpty()
    }

    /**
     * 拦截返回事件
     */
    fun interceptBackEvent() = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (interceptBackEvent()) false else super.onKeyDown(keyCode, event)
        } else super.onKeyDown(keyCode, event)
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.baselib_slide_out_form_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null) unbinder.unbind()
        hideProgressBar()
    }

}