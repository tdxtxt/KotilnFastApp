package com.baselib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.mvp.view.IView
import com.lxj.statelayout.StateLayout
import com.trello.rxlifecycle2.components.RxFragment

abstract class BaseFragment : RxFragment(), IView {
    protected lateinit var mRootView: View
    private var unbinder: Unbinder? = null
    private lateinit var stateLayout: StateLayout

    protected abstract fun getLayoutId(): Int

    open fun getParams(bundle: Bundle){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getParams(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater?.inflate(getLayoutId(),container, false)?:
                View.inflate(activity,getLayoutId(),container)
        unbinder = ButterKnife.bind(this, mRootView)
        return initStateView(mRootView).apply { stateLayout = this }
    }


    /**
     * 多状态通用页面
     */
    open fun initStateView(view: View) = StateLayout(activity)
            /*.config(loadingLayoutId = R.layout.custom_loading, //自定义加载中布局
                    errorLayoutId = R.layout.custom_error, //自定义加载失败布局
                    emptyLayoutId = R.layout.custom_empty, //自定义数据位为空的布局
                    useContentBgWhenLoading = true, //加载过程中是否使用内容的背景
                    enableLoadingShadow = true, //加载过程中是否启用半透明阴影盖在内容上面
                    retryAction = { //点击errorView的回调
                        ToastHelper.showToast("点击重试")
                    })*/
            .wrap(view)
            .showContent()

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    override fun showProgressBar() {
        if(activity is BaseActivity)
            (activity as BaseActivity).showProgressBar()
        else{

        }
    }

    override fun hideProgressBar() {
        if(activity is BaseActivity)
            (activity as BaseActivity).hideProgressBar()
        else{

        }
    }

    override fun showLoadingView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showContentView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCustomView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorView(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}