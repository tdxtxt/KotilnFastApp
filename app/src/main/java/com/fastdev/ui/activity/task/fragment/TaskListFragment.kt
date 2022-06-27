package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.data.response.TaskEntity
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.TaskDetailsActivity
import com.fastdev.ui.activity.task.presenter.TaskListPresenter
import com.fastdev.ui.activity.task.viewmodel.TaskListViewModel
import com.fastdev.ui.adapter.BaseQuickLoadMoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_list.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : BaseMvpFragment(), TaskListPresenter.BaseMvpImpl {
    @Inject
    lateinit var presenter: TaskListPresenter
    var viewModel: TaskListViewModel? = null

    lateinit var adapter: BaseQuickLoadMoreAdapter<TaskEntity, BaseViewHolder>

    var pageNum: Int = 1
    var type: Int = 0
    override fun getParams(bundle: Bundle?) {
        type = bundle?.getInt("type")?: 0
    }

    override fun createPresenter() = presenter

    override fun createMvpView() = this

    override fun getLayoutId() = R.layout.fragment_task_list

    override fun initUi() {
        viewModel = fragmentActivity?.run { TaskListViewModel.get(this) }
        viewModel?.refreshGlobal?.observe(this, Observer {
            reload(null)
        })

        adapter = object : BaseQuickLoadMoreAdapter<TaskEntity, BaseViewHolder>(R.layout.item_task, -1){
            override fun convert(holder: BaseViewHolder, item: TaskEntity) {
                holder.setText(R.id.tv_name, item.task_name)
                        .setText(R.id.tv_status, item.getStatus())
                        .setText(R.id.tv_create_name, item.task_create_by)
                        .setText(R.id.tv_starttime, item.task_time)
                        .setText(R.id.tv_remark, item.task_info)
                        .setText(R.id.tv_num_all, item.task_pd_count)
                        .setText(R.id.tv_num_wait, item.task_wait_count)
                        .setText(R.id.tv_num_finish, item.task_complete_count)
                        .setText(R.id.tv_num_py, item.task_py_count)
                        .setText(R.id.tv_num_pk, item.task_pk_count)
                        .setGone(R.id.btn_next, isHistoryList())

                holder.setTextColorRes(R.id.tv_status, when(item.task_status){
                    TaskEntity.STATUS_ING -> R.color.purple_9829ff
                    TaskEntity.STATUS_FINISH -> R.color.green_09bb07
                    TaskEntity.STATUS_DEPRECATED -> R.color.black_999999
                    TaskEntity.STATUS_WAIT -> R.color.red_f5222d
                    else -> R.color.black_999999
                })
            }
        }
        adapter.addChildClickViewIds(R.id.btn_next)
        adapter.setOnItemChildClickListener { adapt, view, position ->
            if(!isHistoryList()){
                presenter.loadAllSourceByTask(adapter.getItem(position))
            }
        }
        adapter.loadMoreModule.setOnLoadMoreListener {
            load()
        }
        recyclerView.setAdapter(adapter)
        refreshLayout.setOnRefreshListener {
            reload(null)
        }

        getStateView(R.id.refreshLayout)?.showLoading()
        reload(null)
    }

    override fun reload(view: View?) {
        pageNum = 1
        load()
    }

    private fun load(){
        when(type){
            _ING -> presenter.queryCurrentTaskList(pageNum)
            _END -> presenter.queryHistoryTaskList(pageNum)
        }
    }

    override fun fail(errorMsg: String?) {
        refreshLayout.finishRefresh()
        ToastHelper.showToast(errorMsg)
        getStateView(R.id.refreshLayout)?.showError()
    }

    override fun updateView(pageNum: Int, data: MutableList<TaskEntity>?) {
        refreshLayout.finishRefresh()
        if(pageNum == 1 && data?.size == 0) {
            getStateView(R.id.refreshLayout)?.showEmpty()
        }else{
            getStateView(R.id.refreshLayout)?.showContent()
            adapter.updateData(pageNum, data)
        }

        this.pageNum ++
    }

    override fun gotoTaskDetailsActivity(task: TaskEntity) {
        TaskDetailsActivity.open(fragmentActivity, task){
            //刷新数据
            presenter.queryStatusQuantity(task){
                adapter.updateItem(it)
            }
        }
    }

    private fun isHistoryList() = _END == type

    companion object{
        val _ING = 1
        val _END = 2
        fun getIngInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _ING) }) }
        fun getEndInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _END) }) }
    }
}