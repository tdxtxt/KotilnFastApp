package com.baselib.helper

import android.annotation.SuppressLint
import com.baselib.net.NetMgr
import com.baselib.net.error.NetError
import com.baselib.net.model.IModel
import com.baselib.ui.dialog.child.ProgressDialog
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

    /**
     * 绑定activity生命周期，防止内存泄漏
     */
    @SuppressLint("CheckResult")
    fun <T> Flowable<T>.composeError(): Flowable<T> = this.compose{ upstream ->
        upstream.onErrorResumeNext(Function {
            Flowable.empty()
        }).onExceptionResumeNext {
            it.onComplete()
        }
    }

    /**
     * 绑定activity生命周期，防止内存泄漏
     */
    @SuppressLint("CheckResult")
    fun <T,R> Flowable<T>.composeBindLifecycle(lifecycle: LifecycleProvider<R>): Flowable<T> = this.compose(lifecycle.bindToLifecycle()).composeUIThread()


    /**
     * 线程切换
     */
        @SuppressLint("CheckResult")
        fun <T> Flowable<T>.composeUIThread(): Flowable<T> = this.compose(FlowableTransformer<T, T> {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        })

    /**
     * 缓存数据
     */
    @SuppressLint("CheckResult")
    fun <T> Flowable<T>.composeRxcache(key: String): Flowable<T> = this.compose(FlowableTransformer<T, T> {
        it.map { result -> result.apply { CacheHelper.put(key, result) } }
                .onErrorResumeNext(Function { throwable -> Flowable.just(CacheHelper.get(key)) })
                .onExceptionResumeNext {
                    var result: T? = CacheHelper.get(key)
                    it.onNext(result)
                    it.onComplete()
                }
    }).composeUIThread()

    /**
     * 主要用于网络请求
     */
    @SuppressLint("CheckResult")
    fun <T : IModel> Flowable<T>.composeApi(key: String): Flowable<T> = this.compose { upstream ->
        upstream.flatMap { model ->
            when {
                model == null -> Flowable.error(
                        NetError(NetError.NoDataError, model?.getMessage() ?: "数据为空"))
                model.isSuccess() -> Flowable.just(model)
                else -> Flowable.just(model)
            }
        }.onErrorResumeNext(Function {
            var error = NetMgr.getInstance().getProvider().convertError(it)
            if (NetMgr.getInstance().getProvider().handleError(error)) Flowable.empty()
            else when (error.type) {
                NetError.NoDataError,
                NetError.NoConnectError,
                NetError.TimeOutError,
                NetError.ParseError -> {
                    ToastHelper.showToast(error.message)
                    Flowable.error(it)
                }
                else -> Flowable.empty()
            }
        })
    }.composeUIThread()

    /**
     * 统一的弹框转圈进度框事务
     */
    @SuppressLint("CheckResult")
    fun <T> Flowable<T>.composeProgress(progressDialog: ProgressDialog?) = this.compose { upstream ->
        upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progressDialog?.setCancelListener{ it.cancel() }
                    progressDialog?.show()
                }
                .doOnComplete {
                    progressDialog?.dismiss()
                }
                .doOnError {
                    progressDialog?.dismiss()
                }
    }

