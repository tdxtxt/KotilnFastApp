package com.baselib.ui.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/5/8
 */
abstract class BaseViewModel : ViewModel() {
    // Loading 状态
    val isLoading = MutableLiveData(false)
}