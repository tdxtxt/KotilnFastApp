package com.baselib.helper

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.baselib.ui.dialog.child.ProgressDialog
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
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
        it.map { result -> result.apply { CommonCacheHelper.put(key, result) } }
                .onErrorResumeNext(Function { throwable -> Flowable.just(CommonCacheHelper.get(key)) })
                .onExceptionResumeNext {
                    var result: T? = CommonCacheHelper.get(key)
                    it.onNext(result)
                    it.onComplete()
                }
    }).composeUIThread()


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

    fun <T> Observable<T>.lifecycleOwner(owner: LifecycleOwner? = null) = this.compose { upstream ->
        upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    it.apply {
                        val observer = RxjavaLifecycleObserver(::dispose)
                        owner?.lifecycle?.addObserver(observer)
                    }
                }
    }


    internal class RxjavaLifecycleObserver(private val cancel: () -> Unit) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = cancel()
    }

