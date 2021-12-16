package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import com.baselib.ui.fragment.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.TaskDetailsActivity
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : BaseFragment() {
    lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>

    override fun getLayoutId() = R.layout.fragment_task_list

    override fun initUi() {
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_task){
            override fun convert(holder: BaseViewHolder, item: String) {
            }

        }

        adapter.setOnItemClickListener { adapter, view, position ->
            TaskDetailsActivity.open(fragmentActivity)
        }

        recyclerView.setAdapter(adapter)

        adapter.setNewInstance(mutableListOf("", "", "", "", ""))
    }

    companion object{
        val _ING = 1
        val _END = 2
        fun getIngInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _ING) }) }
        fun getEndInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _END) }) }
    }
}