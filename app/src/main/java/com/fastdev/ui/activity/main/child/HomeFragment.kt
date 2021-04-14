package com.fastdev.ui.activity.main.child

import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.fastdev.mvp.HomePresenter
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.fragment_main_home.*

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/10
 */
class HomeFragment : BaseMvpFragment() {
    override fun <V : BaseMvpView> createPresenter(): BaseMvpPresenter<V>? {
        return HomePresenter() as BaseMvpPresenter<V>?
    }

    override fun createMvpView(): BaseMvpView? {
        return this
    }

    override fun getLayoutId() = R.layout.fragment_main_home

    override fun initUi() {
        refreshLayout.setOnRefreshListener {
            refreshLayout.finishRefresh()
        }
    }
}