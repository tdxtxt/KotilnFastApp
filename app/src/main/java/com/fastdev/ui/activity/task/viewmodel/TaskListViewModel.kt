package com.fastdev.ui.activity.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/29
 */
class TaskListViewModel: ViewModel() {
    val refreshGlobal = MutableLiveData<Boolean>()


    companion object{
        fun get(owner: ViewModelStoreOwner) = ViewModelProvider(owner).get(TaskListViewModel::class.java)
    }
}