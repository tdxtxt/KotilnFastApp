package com.fastdev.mvp

import com.baselib.helper.LogA
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.db.Source
import com.fastdev.data.repository.TestRepository
import com.seuic.uhf.UHFService
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/14
 */
class HomePresenter @Inject constructor(val repository: TestRepository) : AbsPresenter<HomePresenter.IViewHome>() {

    interface IViewHome : BaseMvpView{
        fun showXX()
    }

    fun startScanf(){
    }

    fun getData(){
        repository.saveAssets(Source().apply { name = "1111" })
        LogA.i(repository.query()?.name)
        ToastHelper.showToast(repository.query()?.name)
    }
}