package com.baselib.helper

import android.annotation.SuppressLint
import com.baselib.net.NetMgr
import com.baselib.net.error.NetError
import com.baselib.net.model.IModel
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

/**
 * 创建时间： 2020/5/22
 * 编码： tangdex
 * 功能描述:
 */

/**
 * 绑定activity生命周期，防止内存泄漏
 */
@SuppressLint("CheckResult")
fun <T> Flowable<T>.composeBindLifecycle(lifecycle: LifecycleProvider<ActivityEvent>): Flowable<T> = this.compose(lifecycle.bindToLifecycle()).composeUIThread()

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
fun <T : IModel> Flowable<T>.composeApi(key: String): Flowable<T> = this.compose {
    it.flatMap { model ->
        when {
            model == null -> Flowable.error(
                    NetError(NetError.NoDataError, model.getMessage() ?: "数据为空"))
            model.isSuccess() -> Flowable.just(model)
            else -> Flowable.just(model)
        }
    }.onErrorResumeNext(Function { e ->
        var error = NetMgr.getInstance().getProvider().convertError(e)
        if (NetMgr.getInstance().getProvider().handleError(error)) Flowable.empty()
        else when (error.type) {
            NetError.NoDataError,
            NetError.NoConnectError,
            NetError.TimeOutError,
            NetError.ParseError -> {
                ToastHelper.showToast(error.message)
                Flowable.error(e)
            }
            else -> Flowable.empty()
        }
    })
}.composeUIThread()

