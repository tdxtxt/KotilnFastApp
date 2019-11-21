package com.baselib.net.okhttpconfig

import okhttp3.Interceptor
import okhttp3.Response

class NetInterceptor constructor(val handler: RequestHandler) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (handler != null) {
            request = handler.onBeforeRequest(request, chain)
        }
        val response = chain.proceed(request)
        if (handler != null) {
            val tmp = handler.onAfterRequest(response, chain)
            if (tmp != null) {
                return tmp
            }
        }
        return response
    }
}