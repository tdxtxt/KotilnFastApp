package com.baselib.net.okhttpconfig

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

interface RequestHandler {
    //请求前操作
    fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request
    //请求后操作
    @Throws(IOException::class)
    fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response
}