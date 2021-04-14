package com.fastdev.ui.activity.main.child

import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.fastdev.mvp.HomePresenter
import com.fastdev.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.android.synthetic.main.include_top_search.*
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/10
 */
@AndroidEntryPoint
class HomeFragment : BaseMvpFragment(), HomePresenter.IViewHome {
    @Inject
    lateinit var presenter: HomePresenter

    override fun createPresenter(): BaseMvpPresenter<*> {
        return presenter
    }

    override fun createMvpView(): BaseMvpView? {
        return this
    }

    override fun getLayoutId() = R.layout.fragment_main_home

    override fun initUi() {
        refreshLayout.setOnRefreshListener {
            refreshLayout.finishRefresh()
        }

        et_search.setOnClickListener { presenter.getData() }
    }

    override fun showXX() {
        ToastHelper.showToast("xxxxxx")
    }
}