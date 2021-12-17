package com.baselib.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.baselib.R
import com.baselib.helper.DialogHelper
import com.baselib.helper.HashMapParams
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.dialog.child.ProgressDialog
import com.baselib.ui.activity.IView
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle3.components.support.RxFragment
import androidx.fragment.app.FragmentActivity
import com.baselib.rx.transformer.ProgressTransformer
import com.baselib.rx.transformer.UIThreadTransformer
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.FragmentEvent

abstract class BaseFragment : RxFragment(), IView {
    var fragmentActivity: FragmentActivity? = null
    protected lateinit var mRootView: View
    //    private var unbinder: Unbinder? = null
//    private var stateLayout: StateLayout? = null
    private var stateLayouts = SparseArray<StateLayout>()
    private var mProgressDialog: ProgressDialog? = null

    protected abstract fun getLayoutId(): Int

    open fun initUi(){}

    open fun getParams(bundle: Bundle?){}

    open fun reload(view: View?){}

    open fun customConfigSateView(view: View, stateLayout: StateLayout){}

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
        return mRootView //initStateView().apply { stateLayout = this }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
    }

    fun <T : View> findView(resId: Int): T {
        return mRootView.findViewById<View>(resId) as T
    }

    fun getStateView(resId: Int) : StateLayout?{
        var stateLayout: StateLayout? = stateLayouts.get(resId)

        if(stateLayout != null) return stateLayout

        if(resId == mRootView.id){
            throw IllegalArgumentException("作用控件不能为根View，可根据情况在包一层(原因会影响部分功能)")
        }

        val view: View = mRootView.findViewById(resId)?: throw IllegalArgumentException("作用控件不存在，请检查resId是否正确")

        if(fragmentActivity != null){
            stateLayout = StateLayout(fragmentActivity!!)

            initConfigStateView(view, stateLayout)
            stateLayout.showContent()
            stateLayouts.put(resId, stateLayout)
            return stateLayout
        }
        throw IllegalArgumentException("activity is null")
    }

    private fun initConfigStateView(view: View, stateLayout: StateLayout){
        stateLayout.configAll(
                emptyText = "别看了，这里什么都没有",
                loadingLayoutId = R.layout._loading_layout_loading, //自定义加载中布局
                errorLayoutId = R.layout._loading_layout_error, //自定义加载失败布局
                emptyLayoutId = R.layout._loading_layout_empty, //自定义数据位为空的布局
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

    override fun showProgressBar(desc: String, isCancel: Boolean) {
        getProgressBar()?.setDesc(desc)?.setCancelable(isCancel)?.show();
    }

    override fun <T> bindLifecycle(): LifecycleTransformer<T> {
        return this.bindUntilEvent(FragmentEvent.DESTROY);
    }

    override fun <T> bindUIThread(): UIThreadTransformer<T> {
        return UIThreadTransformer()
    }

    override fun <T> bindProgress(): ProgressTransformer<T> {
        return ProgressTransformer(getProgressBar());
    }

    override fun <T> bindProgress(bindDialog: Boolean): ProgressTransformer<T> {
        return ProgressTransformer(getProgressBar(), bindDialog);
    }

    override fun hideProgressBar() {
        getProgressBar()?.hide()
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