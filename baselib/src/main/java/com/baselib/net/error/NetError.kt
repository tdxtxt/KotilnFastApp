package com.baselib.net.error

import com.baselib.app.DevApp
import com.baselib.R

class NetError constructor(var type: Int,cause: Throwable) : Exception(cause) {
    companion object {
        const val ParseError = 0   //数据解析异常
        const val NoConnectError = 1   //无连接异常
        const val AuthError = 2   //用户验证异常
        const val NoDataError = 3   //无数据返回异常
        const val BusinessError = 4   //业务异常
        const val TimeOutError = 5//超时
        const val ServiceError = 6//服务器繁忙，通常就是服务器挂了的表现
        const val UnknownError = 7   //未知异常
    }

    constructor(exception: Throwable) : this(UnknownError,exception)

    constructor(type: Int, message: String) : this(type, Throwable(message))


    private var exception: Throwable? = null

    override fun getLocalizedMessage(): String? {
        return when(type){
            ParseError -> DevApp.mContext?.getString(R.string.baselib_parse_error)
            NoConnectError -> DevApp.mContext?.getString(R.string.baselib_network_error)
            TimeOutError -> DevApp.mContext?.getString(R.string.baselib_timeout_error)
            ServiceError -> DevApp.mContext?.getString(R.string.baselib_service_error)
            AuthError -> ""
            NoDataError -> ""
            BusinessError -> ""
            UnknownError -> exception?.message?: super.message

            else -> exception?.message?: super.message
        }
    }
}