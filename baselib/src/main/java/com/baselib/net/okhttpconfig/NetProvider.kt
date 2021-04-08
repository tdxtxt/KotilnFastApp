package com.baselib.net.okhttpconfig

import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient

//抽象配置操作
interface NetProvider {
    //配置的拦截器
    fun configInterceptors(): Array<Interceptor>?
    //配置Okhttp库
    fun configHttps(builder: OkHttpClient.Builder)
    //配置cookie
    fun configCookie(): CookieJar?
    //配置请求前后操作
    fun configHandler(): RequestHandler?
    //配置连接超时
    fun configConnectTimeoutSecs(): Long
    //配置读超时
    fun configReadTimeoutSecs(): Long
    //配置写超时
    fun configWriteTimeoutSecs(): Long
    //配置是否打印log日志
    fun configLogEnable(): Boolean
}