package com.baselib.ui.mvp.view

interface IView {
    fun hideProgressBar()
    fun showProgressBar()

    fun showLoadingView()
    fun showContentView()
    fun showEmptyView()
    fun showCustomView()
    fun showErrorView(e: Throwable)
}