package com.baselib.net.okhttpconfig

import com.baselib.net.error.NetError
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import okhttp3.*
import org.json.JSONException
import retrofit2.adapter.rxjava2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * 实现配置基类
 */
class BaseNetProvider : NetProvider {
    private var CONNECT_TIME_OUT: Long = 30
    private var READ_TIME_OUT: Long = 180
    private var WRITE_TIME_OUT: Long = 30
    private var isLog = true

    override fun configInterceptors(): Array<Interceptor>? = null

    override fun configHttps(builder: OkHttpClient.Builder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configCookie(): CookieJar? = null

    override fun configHandler(): RequestHandler = HeaderHandler()

    override fun configConnectTimeoutSecs() = CONNECT_TIME_OUT

    override fun configReadTimeoutSecs() = READ_TIME_OUT

    override fun configWriteTimeoutSecs() = WRITE_TIME_OUT

    override fun configLogEnable() = isLog

    override fun handleError(error: NetError): Boolean {
        return NetError.AuthError === error.type
    }

    override fun convertError(e: Throwable) =
            when (e) {
                is NetError -> e
                is UnknownHostException -> NetError(NetError.NoConnectError, e)//断网
                is ConnectException -> NetError(NetError.NoConnectError, e)//主机错误
                is JSONException -> NetError(NetError.ParseError, e)
                is JsonParseException -> NetError(NetError.ParseError, e)
                is JsonSyntaxException -> NetError(NetError.ParseError, e)
                is HttpException -> NetError(NetError.ServiceError, e)
                is retrofit2.HttpException -> NetError(NetError.ServiceError, e)
                is TimeoutException -> NetError(NetError.TimeOutError, e)
                else -> NetError(NetError.UnknownError, e)
            }


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