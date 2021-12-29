package com.fastdev.ui.activity.qrcode.presenter

import android.annotation.SuppressLint
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.SourceBean
import javax.inject.Inject

class ScanQrcodePresenter @Inject constructor(val netRepository: NetApiRepository, val dbRepository: DbApiRepository): AbsPresenter<ScanQrcodePresenter.BaseMvpImpl>() {

    fun netRepository() = netRepository

    fun dbRepository() = dbRepository

    @SuppressLint("CheckResult")
    fun saveSource(taskId: String?, source: SourceBean?, suc: ((SourceBean?) -> Unit)){
        dbRepository.saveOrUpdate(taskId?:"", source)
                .compose(baseView?.bindProgress())
                .subscribe ({
                    suc.invoke(it)
                }){
                    it.printStackTrace()
                }
    }

    interface BaseMvpImpl : BaseMvpView{

    }

}