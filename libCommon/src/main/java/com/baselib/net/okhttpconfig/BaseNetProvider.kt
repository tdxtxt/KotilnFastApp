package com.baselib.net.okhttpconfig

import okhttp3.*

/**
 * 实现配置基类
 */
open class BaseNetProvider : NetProvider {
    private var CONNECT_TIME_OUT: Long = 30
    private var READ_TIME_OUT: Long = 180
    private var WRITE_TIME_OUT: Long = 30
    private var isLog = true

    override fun configInterceptors(): Array<Interceptor>? = null

    override fun configHttps(builder: OkHttpClient.Builder) {
    }

    override fun configCookie(): CookieJar? = null

    override fun configHandler(): RequestHandler = HeaderHandler()

    override fun configConnectTimeoutSecs() = CONNECT_TIME_OUT

    override fun configReadTimeoutSecs() = READ_TIME_OUT

    override fun configWriteTimeoutSecs() = WRITE_TIME_OUT

    override fun configLogEnable() = isLog


    class HeaderHandler : RequestHandler {
        override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {
            return chain.request().newBuilder()
                    .addHeader("Authorization", "tondx")
                    .build()
        }

        override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
            when(response.code()){
                401 -> {} //登录已过期,请重新登录
                403 -> {} //禁止访问
                404 -> {} //链接错误
                500 -> {} //服务器内部错误
                503 -> {} //服务器升级中
            }
            return response
        }
    }

}