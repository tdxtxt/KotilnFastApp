package com.fastdev.ui.activity.main.child

import com.baselib.helper.StatusBarHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.fastdev.helper.chart.StudyPoint
import com.fastdev.mvp.HomePresenter
import com.fastdev.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_home.*
import java.util.*
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
        StatusBarHelper.setStatusBarHeight(activity, mRootView)

        refreshLayout.setOnRefreshListener {
            it.finishRefresh()

            val r = Random()
            val data : MutableList<StudyPoint> = mutableListOf()
            for(i in 1..100){
                data.add(StudyPoint("$i", if(i < 30) 100f else if(i < 60) 50f else 0f, r.nextInt(110).toFloat(), r.nextInt(110).toFloat()))
            }

            lineChart.showJXLineChart(100, 7, 0, 1, data, "100")
        }

        refreshLayout.autoRefresh()
    }

    override fun showXX() {
        ToastHelper.showToast("xxxxxx")
    }

}
