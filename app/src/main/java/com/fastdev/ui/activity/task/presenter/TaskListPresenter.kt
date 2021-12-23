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
                    if(resp.data?.isEmpty() == false && dbRepository.isStartTask()){
                        Flowable.fromIterable(resp.data.getListData())
                                .doAfterNext {
                                    it.task_py_count = dbRepository.queryTaskPyNum(it.task_id)
                                    it.task_pk_count = dbRepository.queryTaskPkNum(it.task_id)
                                    it.task_wait_count = dbRepository.queryTaskWaitNum(it.task_id)
                                    it.task_complete_count = dbRepository.queryTaskFinishNum(it.task_id)
                                }.buffer(resp.data.getListData()?.size?: 0)
                                .map { resp }
                    }else{
                        Flowable.just(resp)
                    }
                }.subscribeWith(object : BaseObserver<TaskEntity>(){
                    override fun onSuccess(response: ResponseBody<TaskEntity>?) {
                        response?.data?.getListData()
                    }
                    override fun onFailure(response: ResponseBody<TaskEntity>?, errorMsg: String?, e: Throwable?) {

                    }
                })

    }

    fun queryHistoryTaskList(pageNum: Int){
        netRepository.queryTaskList("his", pageNum, 20)
    }

    fun loadAllSourceByTask(task: TaskEntity){
        if(dbRepository.isStartTask()){
            baseView?.gotoTaskDetailsActivity()
        }else{
            netRepository.queryAllSourceByTask(task.task_id)
                    .delay(3, TimeUnit.SECONDS)
//                    .doAfterNext {
//                        //存数据库
//
//                    }
                    .compose(baseView?.bindProgress())
                    .subscribeWith(object : BaseObserver<SourceResp>(){
                        override fun onSuccess(response: ResponseBody<SourceResp>?) {
                            ToastHelper.showToast("成功")
                        }
                        override fun onFailure(response: ResponseBody<SourceResp>?, errorMsg: String?, e: Throwable?) {
                            ToastHelper.showToast("失败$errorMsg")
                        }
                    })
        }
    }

    interface BaseMvpImpl : BaseMvpView{
        fun gotoTaskDetailsActivity()
    }
}