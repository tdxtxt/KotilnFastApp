package com.baselib.ui.mvp.view

import com.baselib.ui.dialog.child.NativeProgressDialog

interface IView {
    fun getProgressBar(): NativeProgressDialog?
    fun hideProgressBar()
    fun showProgressBar()

    fun showLoadingView()
    fun showContentView()
    fun showEmptyView()
    fun showCustomView()
    fun showErrorView(e: Throwable)
}