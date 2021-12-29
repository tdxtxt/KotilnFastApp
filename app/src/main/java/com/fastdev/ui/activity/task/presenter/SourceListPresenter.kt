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
class SourceListPresenter @Inject constructor(val netRepository: NetApiRepository, val dbRepository: DbApiRepository): AbsPresenter<SourceListPresenter.BaseMvpImpl>() {

    fun querySourceAll(taskId: String?, pageNum: Int){
        val disposable =
                dbRepository.querySourceAll(taskId, pageNum)
                        .compose(baseView?.bindLifecycle())
                        .subscribe {
                            baseView?.updateView(pageNum, it)
                        }
    }

    fun querySourceByWait(taskId: String?, pageNum: Int){
        val disposable =
                dbRepository.querySourceByWait(taskId, pageNum)
                        .compose(baseView?.bindLifecycle())
                        .subscribe {
                            baseView?.updateView(pageNum, it)
                        }
    }

    fun querySourceByPY(taskId: String?, pageNum: Int){
        val disposable =
        dbRepository.querySourceByPY(taskId, pageNum)
                .compose(baseView?.bindLifecycle())
                .subscribe {
                    baseView?.updateView(pageNum, it)
                }
    }

    fun querySourceByPK(taskId: String?, pageNum: Int){
        val disposable =
                dbRepository.querySourceByPK(taskId, pageNum)
                        .compose(baseView?.bindLifecycle())
                        .subscribe {
                            baseView?.updateView(pageNum, it)
                        }
    }

    fun querySourceByFinish(taskId: String?, pageNum: Int){
        val disposable =
                dbRepository.querySourceByFinish(taskId, pageNum)
                        .compose(baseView?.bindLifecycle())
                        .subscribe {
                            baseView?.updateView(pageNum, it)
                        }
    }

    fun updateSource(taskId: String?, source: SourceBean?, callback: (SourceBean?) -> Unit){
        val disposable =
            dbRepository.update(taskId, source)
                    .compose(baseView?.bindLifecycle())
                    .subscribe {
                        callback.invoke(it)
                    }
    }

    fun queryStatusQuantity(taskId: String?, callback: (quantity: Quantity) -> Unit){
        val disposable =
        dbRepository.queryStatusQuantity(taskId)
                .compose(baseView?.bindLifecycle())
                .subscribe {
                    callback.invoke(it)
                }
    }

    interface BaseMvpImpl: BaseMvpView{
        fun updateView(pageNum: Int, data: MutableList<SourceBean>?)
    }
}