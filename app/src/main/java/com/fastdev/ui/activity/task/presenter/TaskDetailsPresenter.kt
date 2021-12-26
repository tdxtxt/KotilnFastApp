package com.fastdev.ui.activity.task.presenter

import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.SourceBean
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

    interface BaseMvpImpl: BaseMvpView{
    }
}