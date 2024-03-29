package com.fastdev.mvp

import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.repository.NetApiRepository
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/14
 */
class HomePresenter @Inject constructor(val repository: NetApiRepository) : AbsPresenter<HomePresenter.IViewHome>() {

    interface IViewHome : BaseMvpView{
        fun showXX()
    }

    fun startScanf(){
    }

    fun getData(){
    }
}