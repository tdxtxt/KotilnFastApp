package com.fastdev.ui.activity.login.presenter

import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.ResponseBody
import com.fastdev.net.observer.BaseObserver
import com.fastdev.data.repository.NetApiRepository
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class LoginPresenter @Inject constructor(val repository: NetApiRepository) : AbsPresenter<LoginPresenter.BaseMvpImpl>() {

    fun login(accout: String, pwd: String){
        val disposable = repository.login(accout, pwd)
                .compose(baseView?.bindProgress())
                .subscribeWith(object : BaseObserver<String>(){
                    override fun onSuccess(response: ResponseBody<String>?) {
                    }
                    override fun onFailure(response: ResponseBody<String>?, errorMsg: String?, e: Throwable?) {
                    }
                })
    }

    interface BaseMvpImpl : BaseMvpView{

    }
}