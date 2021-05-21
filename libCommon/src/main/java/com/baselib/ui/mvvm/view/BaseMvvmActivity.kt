package com.baselib.ui.mvvm.view

import android.os.Bundle
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.mvvm.vm.BaseViewModel

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/5/8
 */
abstract class BaseMvvmActivity : BaseActivity(){
    abstract fun createPresenter(): BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createPresenter().apply {
            isLoading.observeForever {
                if (it) showProgressBar()
                else hideProgressBar()
            }
        }
    }
}