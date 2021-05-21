package com.baselib.ui.activity

import com.baselib.ui.dialog.child.ProgressDialog

interface IView {
    fun getProgressBar(): ProgressDialog?
    fun hideProgressBar()
    fun showProgressBar()

    fun showLoadingView()
    fun showContentView()
    fun showEmptyView()
    fun showErrorView(e: Throwable)
}