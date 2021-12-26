package com.baselib.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.baselib.R
import com.baselib.app.ApplicationDelegate
import com.baselib.callback.StartForResultListener
import com.baselib.helper.DialogHelper
import com.baselib.helper.HashMapParams
import com.baselib.helper.LogA
import com.baselib.helper.StatusBarHelper
import com.baselib.rx.transformer.ProgressTransformer
import com.baselib.rx.transformer.UIThreadTransformer
import com.baselib.ui.dialog.child.ProgressDialog
import com.baselib.ui.fragment.impl.StartForResultFragment1
import com.baselib.ui.fragment.impl.StartForResultFragment2
import com.fast.libdeveloper.AppContainer
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


/**
 * @作者： ton
 * @创建时间： 2018\11\30
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
abstract class BaseActivity : RxAppCompatActivity(), IView {
    private var appcontainer : AppContainer? = ApplicationDelegate.delegate.getAppContainer()
    protected lateinit var fragmentActivity: FragmentActivity
    private var mProgressDialog: ProgressDialog? = null
    protected var interceptBackEvent = false
    protected var interceptCallBack: (() -> Unit)? = null
    private val stateLayouts = SparseArray<StateLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentActivity = this
        parseParams(intent) //解析参数
        val rootView = appcontainer?.bind(this)?: findViewById(android.R.id.content)
        if(getLayoutResId() > 0) layoutInflater.inflate(getLayoutResId(), rootView)
        initStatusBar()
        initUi()
//        overridePendingTransition(R.anim.baselib_slide_in_foBrm_right, 0)//进入的切换动画
    }

    abstract fun getLayoutResId(): Int
    open fun initUi(){}
    open fun reload(view: View?){}
    open fun customConfigSateView(view: View, stateLayout: StateLayout){}
    /*private fun createViewBinding(): View {
        val superclass = javaClass.genericSuperclass
        val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        try {
            val method: Method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            mBindingView = method.invoke(null, layoutInflater) as SV
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return mBindingView.root
    }*/

    private fun parseParams(intent: Intent?) {
        if (intent == null) return
        var extraBundle: Bundle? = intent.getBundleExtra("Bundle")
        if (extraBundle == null) extraBundle = intent.extras
        getParams(extraBundle)
    }
    open fun getParams(bundle: Bundle?){}

    /**
     * 多状态通用页面
     */
    fun getStateView(resId: Int) : StateLayout?{
        var stateLayout: StateLayout? = stateLayouts.get(resId)

        if(stateLayout != null) return stateLayout

        val view: View = findViewById(resId)?: (findViewById<View>(android.R.id.content).apply { resId == android.R.id.content })

        stateLayout = StateLayout(fragmentActivity!!)

        initConfigStateView(view, stateLayout)
        stateLayout.showContent()
        stateLayouts.put(resId, stateLayout)
        return stateLayout
    }

    private fun initConfigStateView(view: View, stateLayout: StateLayout){
        stateLayout.configAll(
                emptyText = "别看了，这里什么都没有",
                loadingLayoutId = R.layout.baselib_layout_loading, //自定义加载中布局
                errorLayoutId = R.layout.baselib_layout_error, //自定义加载失败布局
                emptyLayoutId = R.layout.baselib_layout_empty, //自定义数据位为空的布局
                useContentBgWhenLoading = true, //加载过程中是否使用内容的背景
                retryAutoLoading = true,
//                enableLoadingShadow = true, //加载过程中是否启用半透明阴影盖在内容上面
                retryAction = {
                    //点击errorView的回调
                    reload(view)
                })

        customConfigSateView(view, stateLayout)
        stateLayout.wrap(view)
    }

    /**
     * 状态栏
     */
    open fun initStatusBar(){
        StatusBarHelper.transparentStatusBar(fragmentActivity)
        StatusBarHelper.setDarkMode(fragmentActivity)
    }

    override fun getProgressBar(): ProgressDialog? {
        if (mProgressDialog == null) mProgressDialog = DialogHelper.createProgressDialog(this, "正在加载...", true)
        return mProgressDialog?.setDesc("正在加载...")?.apply { setCancelable(true) }
    }

    override fun hideProgressBar() {
        getProgressBar()?.hide()
    }

    override fun showProgressBar() {
        if (isFinishing) return
        getProgressBar()?.setCancelableOnTouchOutside(false)?.show()
    }

    override fun showProgressBar(desc: String, isCancel: Boolean) {
        getProgressBar()?.setDesc(desc)?.setCancelable(isCancel)?.show();
    }

    override fun <T> bindLifecycle(): LifecycleTransformer<T> {
        return this.bindUntilEvent(ActivityEvent.DESTROY);
    }

    override fun <T> bindUIThread(): UIThreadTransformer<T> {
        return UIThreadTransformer()
    }

    override fun <T> bindProgress(): ProgressTransformer<T> {
        return ProgressTransformer(getProgressBar())
    }

    override fun <T> bindProgress(bindDialog: Boolean): ProgressTransformer<T> {
        return ProgressTransformer(getProgressBar(), bindDialog)
    }

    open fun <T : Activity> getActivityNew(): T? = fragmentActivity as T

    /**
     * 拦截返回事件
     */
    fun setInterceptBackEvent(
            interceptBackEvent: Boolean,
            interceptCallBack: (() -> Unit)? = null
    ) {
        this.interceptBackEvent = interceptBackEvent
        this.interceptCallBack = interceptCallBack
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            interceptCallBack?.invoke()
            if (interceptBackEvent) false else super.onKeyDown(keyCode, event)
        } else super.onKeyDown(keyCode, event)
    }

    fun startActivityForResult(clazz: Class<*>, listener: (StartForResultListener.() -> Unit)?){
        startActivityForResult(clazz, null, listener)
    }

    fun startActivityForResult(clazz: Class<*>, params: HashMapParams?, listener: (StartForResultListener.() -> Unit)?){
        startActivityForResult(clazz, params, listener)
    }

    companion object{
        fun Activity.startActivityForResult(clazz: Class<*>, params: HashMapParams?, listener: (StartForResultListener.() -> Unit)?){
            if (!isActivityValid(this)) {
                LogA.i("startActivityForResult ------>  Activity is null or has finished")
                return
            }
            if(this is FragmentActivity){
                val fragment: StartForResultFragment1 = (supportFragmentManager.findFragmentByTag("__start_for_result")?:
                StartForResultFragment1().apply {
                    supportFragmentManager.beginTransaction().add(this, "__start_for_result").commitAllowingStateLoss()
                    supportFragmentManager.executePendingTransactions()
                }) as StartForResultFragment1

                fragment.setListener(listener)
                val intent = Intent()
                if(params != null) intent.putExtra("Bundle", params?.toBundle())
                intent.setClass(this, clazz)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)//单例
                fragment.startActivityForResult(intent, 0x001122)
            }else{
                val fragment: StartForResultFragment2 = (fragmentManager.findFragmentByTag("__start_for_result")?:
                StartForResultFragment2().apply {
                    fragmentManager.beginTransaction().add(this, "__start_for_result").commitAllowingStateLoss()
                    fragmentManager.executePendingTransactions()
                }) as StartForResultFragment2

                fragment.setListener(listener)
                val intent = Intent()
                if(params != null) intent.putExtra("Bundle", params?.toBundle())
                intent.setClass(this, clazz)
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