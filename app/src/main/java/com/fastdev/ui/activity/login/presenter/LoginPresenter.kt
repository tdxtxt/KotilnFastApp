package com.fastdev.ui.activity.login.presenter

import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.ResponseBody
import com.fastdev.net.observer.BaseObserver
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.LoginEntity
import org.litepal.LitePal
import org.litepal.LitePalDB
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
                .subscribeWith(object : BaseObserver<LoginEntity>(){
                    override fun onSuccess(response: ResponseBody<LoginEntity>?) {
                        response?.data?.getData()?.apply {
                            baseView?.loginSuc(this)
                        }?: ToastHelper.showToast("登录信息为空")
                    }
                    override fun onFailure(response: ResponseBody<LoginEntity>?, errorMsg: String?, e: Throwable?) {
                        ToastHelper.showToast(errorMsg)
                    }
                })
    }

    interface BaseMvpImpl : BaseMvpView{
        fun loginSuc(data: LoginEntity)
    }
}