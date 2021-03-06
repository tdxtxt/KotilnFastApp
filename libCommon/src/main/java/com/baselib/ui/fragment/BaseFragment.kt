package com.baselib.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baselib.R
import com.baselib.helper.DialogHelper
import com.baselib.helper.HashMapParams
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.dialog.child.ProgressDialog
import com.baselib.ui.activity.IView
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle3.components.support.RxFragment
import androidx.fragment.app.FragmentActivity

abstract class BaseFragment : RxFragment(), IView {
    var fragmentActivity: FragmentActivity? = null
    protected lateinit var mRootView: View
//    private var unbinder: Unbinder? = null
    private var stateLayout: StateLayout? = null
    private var mProgressDialog: ProgressDialog? = null

    protected abstract fun getLayoutId(): Int

    open fun getParams(bundle: Bundle?){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getParams(arguments)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(getLayoutId(),container, false)?:
                View.inflate(fragmentActivity,getLayoutId(),container)
        mRootView.isClickable = true //截断点击时间段扩散，防止多Fragment出现重叠以及点击穿透
//        unbinder = ButterKnife.bind(this, mRootView)
        return initStateView().apply { stateLayout = this }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
    }

    fun <T : View> findView(resId: Int): T {
        return mRootView.findViewById<View>(resId) as T
    }

    open fun initUi(){}

    open fun configStateView(view: View?, stateLayout: StateLayout){
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
    open fun initStateView(): StateLayout? = fragmentActivity?.run {
        StateLayout(this).apply {
            configStateView(mRootView, this)
        }.showContent()
    }

    open fun <T : Activity> getActivityNew(): T? = fragmentActivity as T?

    override fun getProgressBar(): ProgressDialog? {
        if (mProgressDialog == null){
            mProgressDialog = if(fragmentActivity is BaseActivity) (fragmentActivity as BaseActivity).getProgressBar() else DialogHelper.createProgressDialog(fragmentActivity as FragmentActivity, "请耐心等待，正在处理...", true)
        }
        return mProgressDialog
    }

    override fun showProgressBar() {
        getProgressBar()?.show()
    }

    override fun hideProgressBar() {
        getProgressBar()?.hide()
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
        stateLayout?.showError()
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        unbinder?.unbind()
        getProgressBar()?.dismiss()
    }

    companion object {
        fun <T : BaseFragment> newInstance(clazz: Class<T>): T? {
            return newInstance(clazz, null)
        }

        fun <T : BaseFragment> newInstance(clazz: Class<T>?, params: HashMapParams?): T? {
            var instance: T? = null
            if (clazz != null) {
                try {
                    instance = clazz.newInstance()
                } catch (e: java.lang.InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
            if (params != null) {
                instance!!.arguments = params!!.toBundle()
            }
            return instance
        }
    }

}