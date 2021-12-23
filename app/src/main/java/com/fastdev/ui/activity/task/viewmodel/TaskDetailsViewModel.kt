package com.fastdev.ui.activity.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastdev.data.response.SourceBean

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/22
 */
class TaskDetailsViewModel : ViewModel(){
    val sourceViewModel = MutableLiveData<SourceBean>()

}