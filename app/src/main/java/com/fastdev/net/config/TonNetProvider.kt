package com.fastdev.net.config

import com.baselib.helper.CommonCacheHelper
import com.baselib.net.okhttpconfig.BaseNetProvider
import com.fastdev.helper.getToken
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 */
class TonNetProvider : BaseNetProvider() {
    override fun configInterceptors(): Array<Interceptor>? {
        return arrayOf(ParamsInterceptor(), HeadInterceptor(), ResponseInterceptor())
    }

    internal class ParamsInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val oldRequest = chain.request()
            val newRequestBuilder = oldRequest.newBuilder()
            if ("GET".equals(oldRequest.method(), ignoreCase = true)) {
                val httpUrlBuilder = oldRequest.url().newBuilder()
//                httpUrlBuilder.addQueryParameter("token", CommonCacheHelper.getToken())

                newRequestBuilder.url(httpUrlBuilder.build())
            } else {
                val formBodyBuilder = FormBody.Builder()
//                formBodyBuilder.add("token", CommonCacheHelper.getToken())
                val oldFormBody = oldRequest.body() as FormBody?
                if (oldFormBody != null && oldFormBody.size() > 0) {
                    for (i in 0 until oldFormBody.size()) {
                        formBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i))
                    }
                }
                newRequestBuilder.post(formBodyBuilder.build())
            }
            return chain.proceed(newRequestBuilder.build())
        }
    }

    /**
     * 统一添加head头
     */
    internal class HeadInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val oldRequest = chain.request()
            val newRequestBuilder = oldRequest.newBuilder()
            newRequestBuilder.addHeader("token", CommonCacheHelper.getToken())
            return chain.proceed(newRequestBuilder.build())
        }
    }

    /**
     * 统一处理返回body数据
     */
    internal class ResponseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val cookie = response.header("Set-Cookie")
            return response
        }
    }
}