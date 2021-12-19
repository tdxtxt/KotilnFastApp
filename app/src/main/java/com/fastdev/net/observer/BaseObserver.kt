package com.fastdev.net.observer

import com.baselib.net.NetMgr
import com.fastdev.data.ResponseBody
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber

/**
 * 功能描述: 统一解析数据报文
 * @author tangdexiang
 * @since 2021/4/7
 */
abstract class BaseObserver<T>: DisposableSubscriber<ResponseBody<T>>() {
    abstract fun onSuccess(response: ResponseBody<T>?)

    abstract fun onFailure(response: ResponseBody<T>?, errorMsg: String?, e: Throwable?)

    fun customHandleResp(response: ResponseBody<T>?){}

    override fun onNext(response: ResponseBody<T>) {
        customHandleResp(response)
        onComplete()

        if(response.isSuccess()){
            onSuccess(response)
        }else{
            onFailure(response, response.msg, null)
        }
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        val body: ResponseBody<T> = ResponseBody("-999", NetMgr.getInstance().handleError(e))
        customHandleResp(body)

        onFailure(body, body.msg, e)

        onComplete()
    }
}