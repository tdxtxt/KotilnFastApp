package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import android.view.View
import com.baselib.ui.fragment.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.data.response.TaskEntity
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.presenter.TaskListPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_list.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : BaseFragment() {
    @Inject
    lateinit var presenter: TaskListPresenter

    lateinit var adapter: BaseQuickAdapter<TaskEntity, BaseViewHolder>

    var pageNum: Int = 1
    var type: Int = 0
    override fun getParams(bundle: Bundle?) {
        type = bundle?.getInt("type")?: 0
    }

    override fun getLayoutId() = R.layout.fragment_task_list

    override fun initUi() {
        adapter = object : BaseQuickAdapter<TaskEntity, BaseViewHolder>(R.layout.item_task){
            override fun convert(holder: BaseViewHolder, item: TaskEntity) {
            }
        }

        adapter.setOnItemClickListener { adapt, view, position ->
            if(!isHistoryList()){
                presenter.loadAllSourceByTask(adapter.getItem(position))
                //TaskDetailsActivity.open(fragmentActivity)
            }
        }

        recyclerView.setAdapter(adapter)

        adapter.setNewInstance(mutableListOf(TaskEntity(), TaskEntity(), TaskEntity(), TaskEntity(), TaskEntity()))

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

    fun isHistoryList() = _END == type

    companion object{
        val _ING = 1
        val _END = 2
        fun getIngInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _ING) }) }
        fun getEndInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _END) }) }
    }
}