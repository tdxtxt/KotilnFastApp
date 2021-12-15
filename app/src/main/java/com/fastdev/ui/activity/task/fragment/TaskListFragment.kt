package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R

class TaskListFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_task_list

    companion object{
        val _ING = 1
        val _END = 2
        fun getIngInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _ING) }) }
        fun getEndInstance() = TaskListFragment().apply { setArguments(Bundle().apply { putInt("type", _END) }) }
    }
}