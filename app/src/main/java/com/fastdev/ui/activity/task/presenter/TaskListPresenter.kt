package com.fastdev.ui.activity.task.presenter

import android.annotation.SuppressLint
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.ResponseBody
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.data.response.SourceResp
import com.fastdev.data.response.TaskEntity
import com.fastdev.helper.UserCacheHelper
import com.fastdev.net.observer.BaseObserver
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/20
 */
@SuppressLint("CheckResult")
class TaskListPresenter @Inject constructor(val netRepository: NetApiRepository, val dbRepository: DbApiRepository) : AbsPresenter<TaskListPresenter.BaseMvpImpl>() {
    fun queryCurrentTaskList(pageNum: Int){
        netRepository.queryTaskList("cur", pageNum, 20)
                .flatMap { resp ->
                    if(resp.data?.isEmpty() == false){
                        Flowable.fromIterable(resp.data.getListData())
                                .flatMap { taskEntity ->
                                    if(dbRepository.isStartTask(taskEntity.task_id)){
                                        dbRepository.queryStatusQuantity(taskEntity.task_id).map {
                                            taskEntity.apply {
                                                task_py_count = "${it.py_count}"
                                                task_pk_count = "${it.pk_count}"
                                                task_wait_count = "${it.wait_count}"
                                                task_complete_count = "${it.finish_count}"
                                            }
                                        }
                                    }else{
                                        Flowable.just(taskEntity)
                                    }
                                 }
                                .buffer(resp.data.getListData()?.size?: 0)
                                .map { resp }
                    }else{
                        Flowable.just(resp)
                    }
                }.compose(baseView?.bindLifecycle()).compose(baseView?.bindUIThread())
                .subscribeWith(object : BaseObserver<TaskEntity>(){
                    override fun onSuccess(response: ResponseBody<TaskEntity>?) {
                        baseView?.updateView(pageNum, response?.data?.getListData())
                    }
                    override fun onFailure(response: ResponseBody<TaskEntity>?, errorMsg: String?, e: Throwable?) {
                        if(pageNum == 1) baseView?.fail(errorMsg)
                    }
                })
    }

    fun queryHistoryTaskList(pageNum: Int){
        netRepository.queryTaskList("his", pageNum, 20)
                .compose(baseView?.bindUIThread()).compose(baseView?.bindLifecycle())
                .subscribeWith(object : BaseObserver<TaskEntity>(){
                    override fun onSuccess(response: ResponseBody<TaskEntity>?) {
                        baseView?.updateView(pageNum, response?.data?.getListData())
                    }
                    override fun onFailure(response: ResponseBody<TaskEntity>?, errorMsg: String?, e: Throwable?) {
                        if(pageNum == 1) baseView?.fail(errorMsg)
                    }
                })
    }

    fun queryStatusQuantity(task: TaskEntity, action: (TaskEntity) -> Unit){
        Flowable.just(task).flatMap {
            dbRepository.queryStatusQuantity(it.task_id).map { quantity ->
                it.apply {
                    task_py_count = "${quantity.py_count}"
                    task_pk_count = "${quantity.pk_count}"
                    task_wait_count = "${quantity.wait_count}"
                    task_complete_count = "${quantity.finish_count}"
                }
            }
        }.compose(baseView?.bindProgress()).subscribe {
            action.invoke(it)
        }
    }

    fun loadAllSourceByTask(task: TaskEntity){
        if(dbRepository.isStartTask(task.task_id)){
            baseView?.gotoTaskDetailsActivity(task)
        }else{
            netRepository.queryAllSourceByTask(task.task_id)
                    .filter {
                        if(it.isSuccess()){
                            //资源存数据库
                            dbRepository.syncSave(task.task_id, it.data?.getData())
                        }else{
                            ToastHelper.showToast(it.getMessage())
                            false
                        }
                    }
                    .compose(baseView?.bindUIThread())
                    .compose(baseView?.bindProgress())
                    .subscribeWith(object : BaseObserver<SourceResp>(){
                        override fun onSuccess(response: ResponseBody<SourceResp>?) {
                            baseView?.gotoTaskDetailsActivity(task)
                        }
                        override fun onFailure(response: ResponseBody<SourceResp>?, errorMsg: String?, e: Throwable?) {
                            ToastHelper.showToast("失败$errorMsg")
                        }
                    })
        }
    }

    interface BaseMvpImpl : BaseMvpView{
        fun fail(errorMsg: String?)
        fun updateView(pageNum: Int, data: MutableList<TaskEntity>?)
        fun gotoTaskDetailsActivity(task: TaskEntity)
    }
}