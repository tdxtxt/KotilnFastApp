package com.fastdev.ui.activity.task.presenter

import android.annotation.SuppressLint
import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.ResponseBody
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.TaskEntity
import com.fastdev.net.observer.BaseObserver
import io.reactivex.Flowable
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
                                    it.task_py_count = dbRepository.queryTaskPyNumByTask(it.task_id)
                                    it.task_pk_count = dbRepository.queryTaskPkNumByTask(it.task_id)
                                    it.task_wait_count = dbRepository.queryTaskWaitNumByTask(it.task_id)
                                    it.task_complete_count = dbRepository.queryCompletePkNumByTask(it.task_id)
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

    interface BaseMvpImpl : BaseMvpView{

    }
}