package com.baselib.ui.activity

import com.baselib.rx.transformer.ProgressTransformer
import com.baselib.rx.transformer.UIThreadTransformer
import com.baselib.ui.dialog.child.ProgressDialog
import com.trello.rxlifecycle3.LifecycleTransformer

interface IView {
    fun getProgressBar(): ProgressDialog?
    fun hideProgressBar()
    fun showProgressBar()
    fun showProgressBar(desc: String, isCancel: Boolean)

    fun <T> bindLifecycle(): LifecycleTransformer<T>
    fun <T> bindUIThread(): UIThreadTransformer<T>
    fun <T> bindProgress(): ProgressTransformer<T>
    fun <T> bindProgress(bindDialog: Boolean): ProgressTransformer<T>
//    fun showLoadingView()
//    fun showContentView()
//    fun showEmptyView()
//    fun showErrorView(e: Throwable)
}