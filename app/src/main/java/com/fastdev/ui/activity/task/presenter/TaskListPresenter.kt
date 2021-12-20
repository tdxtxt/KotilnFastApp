package com.fastdev.ui.activity.task.presenter

import com.baselib.ui.mvp.presenter.AbsPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.fastdev.data.repository.NetApiRepository
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/20
 */
class TaskListPresenter @Inject constructor(val netRepository: NetApiRepository) : AbsPresenter<TaskListPresenter.BaseMvpImpl>() {

    fun queryCurrentTaskList(pageNum: Int){
        netRepository.queryTaskList("cur", pageNum, 20)
    }

    fun queryHistoryTaskList(pageNum: Int){
        netRepository.queryTaskList("his", pageNum, 20)
    }

    interface BaseMvpImpl : BaseMvpView{

    }
}