package com.baselib.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.baselib.R
import com.baselib.callback.StartForResultListener
import com.baselib.helper.DialogHelper
import com.baselib.helper.HashMapParams
import com.baselib.helper.LogA
import com.baselib.helper.StatusBarHelper
import com.baselib.ui.dialog.child.ProgressDialog
import com.baselib.ui.fragment.impl.StartForResultFragment1
import com.baselib.ui.fragment.impl.StartForResultFragment2
import com.baselib.ui.mvp.view.IView
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity


/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
abstract class BaseActivity : RxAppCompatActivity(),IView {
    protected lateinit var activity: FragmentActivity
    private var mProgressDialog: ProgressDialog? = null
    private var stateLayout: StateLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        parseParams(intent) //解析参数
        if(getLayoutResId() > 0) setContentView(getLayoutResId())
        initStatusBar()
        stateLayout = initStateView()
        initUi()
//        overridePendingTransition(R.anim.baselib_slide_in_form_right, 0)//进入的切换动画
    }

    private fun parseParams(intent: Intent?) {
        if (intent == null) return
        var extraBundle: Bundle? = intent.getBundleExtra("Bundle")
        if (extraBundle == null) extraBundle = intent.extras
        getParams(extraBundle)
    }
    fun getParams(bundle: Bundle?){}


    open fun configStateView(view: View, stateLayout: StateLayout){
        stateLayout.apply {
            config(loadingLayoutId = R.layout._loading_layout_loading, //自定义加载中布局
                    errorLayoutId = R.layout._loading_layout_error, //自定义加载失败布局
                    emptyLayoutId = R.layout._loading_layout_empty, //自定义数据位为空的布局
                    useContentBgWhenLoading = true, //加载过程中是否使用内容的背景
                    enableLoadingShadow = true, //加载过程中是否启用半透明阴影盖在内容上面
                    retryAction = {
                        //点击errorView的回调

                    })
        }.wrap(view)
    }

    /**
     * 多状态通用页面
     */
    open fun initStateView(): StateLayout? = StateLayout(this).apply {
        configStateView((activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0), this)
    }.showContent()

    /**
     * 状态栏
     */
    open fun initStatusBar(){
        StatusBarHelper.transparentStatusBar(activity)
    }

    abstract fun getLayoutResId(): Int
    open fun initUi(){}

    override fun getProgressBar(): ProgressDialog? {
        if (mProgressDialog == null) mProgressDialog = DialogHelper.createProgressDialog(this, "请耐心等待，正在处理...", true)
        return mProgressDialog
    }

    override fun showProgressBar() {
        if (isFinishing) return
        getProgressBar()?.setCancelableOnTouchOutside(false)?.show()
    }

    override fun hideProgressBar() {
        mProgressDialog?.dismiss()
    }

    override fun showLoadingView() {
        stateLayout?.showLoading()
    }

    override fun showContentView() {
        stateLayout?.showContent()
    }

    override fun showEmptyView() {
        stateLayout?.showEmpty()
    }

    override fun showErrorView(e: Throwable) {
        stateLayout?.showEmpty()
    }

    /**
     * 拦截返回事件
     */
    open fun interceptBackEvent() = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (interceptBackEvent()) false else super.onKeyDown(keyCode, event)
        } else super.onKeyDown(keyCode, event)
    }

    fun startActivityForResult(clazz: Class<*>, listener: (StartForResultListener.() -> Unit)?){
        startActivityForResult(this, clazz, null, listener)
    }

    fun startActivityForResult(clazz: Class<*>, params: HashMapParams?, listener: (StartForResultListener.() -> Unit)?){
        startActivityForResult(this, clazz, params, listener)
    }

    companion object{
        fun startActivityForResult(activity: Activity?, clazz: Class<*>, params: HashMapParams?, listener: (StartForResultListener.() -> Unit)?){
            if(activity == null) return
            if (!isActivityValid(activity)) {
                LogA.i("startActivityForResult ------>  Activity is null or has finished")
                return
            }
            if(activity is FragmentActivity){
                var fragment: StartForResultFragment1 = (activity.supportFragmentManager.findFragmentByTag("__start_for_result")?:
                StartForResultFragment1().apply {
                    activity.supportFragmentManager.beginTransaction().add(this, "__start_for_result").commitAllowingStateLoss()
                    activity.supportFragmentManager.executePendingTransactions()
                }) as StartForResultFragment1

                fragment.setListener(listener)
                val intent = Intent()
                if(params != null) intent.putExtra("Bundle", params?.toBundle())
                intent.setClass(activity, clazz)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)//单例
                fragment.startActivityForResult(intent, 0x001122)
            }else{
                var fragment: StartForResultFragment2 = (activity.fragmentManager.findFragmentByTag("__start_for_result")?:
                StartForResultFragment2().apply {
                    activity.fragmentManager.beginTransaction().add(this, "__start_for_result").commitAllowingStateLoss()
                    activity.fragmentManager.executePendingTransactions()
                }) as StartForResultFragment2

                fragment.setListener(listener)
                val intent = Intent()
                if(params != null) intent.putExtra("Bundle", params?.toBundle())
                intent.setClass(activity, clazz)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)//单例
                fragment.startActivityForResult(intent, 0x001122)
            }

        }
        private fun isActivityValid(activity: Activity?): Boolean {
            if (activity == null || activity.isFinishing) {
                return false
            }
            return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed)
        }
    }

    override fun finish() {
        super.finish()
//        overridePendingTransition(0, R.anim.baselib_slide_out_form_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgressBar()
    }

}