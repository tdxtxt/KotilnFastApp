package com.fastdev.ui.activity.task.presenter

import com.baselib.helper.ToastHelper
import com.baselib.rx.event.RxBus
import com.baselib.rx.event.RxEventCode
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.ResponseBody
import com.fastdev.data.event.TaskEventCode
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.net.observer.BaseObserver
import com.fastdev.ui.activity.task.viewmodel.Quantity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/25
 */
class TaskDetailsPresenter @Inject constructor(val netRepository: NetApiRepository, val dbRepository: DbApiRepository): AbsPresenter<TaskDetailsPresenter.BaseMvpImpl>() {

    fun queryStatusQuantity(taskId: String?, callback: (quantity: Quantity) -> Unit){
        val disposable =
        dbRepository.queryStatusQuantity(taskId)
                .compose(baseView?.bindLifecycle())
                .subscribe {
                    callback.invoke(it)
                }
    }

    fun commitAllSource(taskId: String?){
        val disposable =
            dbRepository.querySourceAll(taskId).observeOn(Schedulers.single())
                    .flatMap {
                        netRepository.postAllSource(taskId, it)
                    }.compose(baseView?.bindProgress())
                    .subscribeWith(object : BaseObserver<Any>(){
                        override fun onSuccess(response: ResponseBody<Any>?) {
                            baseView?.commitSuc()
                        }
                        override fun onFailure(response: ResponseBody<Any>?, errorMsg: String?, e: Throwable?) {
                            ToastHelper.showToast(errorMsg)
                        }
                    })
    }

    fun queryPlaceList(taskId: String?) = dbRepository.queryPlaceList(taskId)

    fun dbRepository() = dbRepository

    interface BaseMvpImpl: BaseMvpView{
        fun commitSuc()
    }
}