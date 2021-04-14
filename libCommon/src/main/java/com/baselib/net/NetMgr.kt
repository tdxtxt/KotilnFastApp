package com.baselib.net

import android.text.TextUtils
import com.baselib.net.log.HttpLogger
import com.baselib.net.okhttpconfig.NetInterceptor
import com.baselib.net.okhttpconfig.NetProvider
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLException

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
class NetMgr {
    private lateinit var sProvider: NetProvider
    private var providerMap = HashMap<String, NetProvider>()
    private var retrofitMap = HashMap<String, Retrofit>()
    private var clientMap = HashMap<String, OkHttpClient>()

    //必须注册调用此接口
    fun registerProvider(provider: NetProvider) {
        this.sProvider = provider
    }

    fun getProvider() = sProvider

    //获取service服务
    fun <S> get(baseUrl: String, service: Class<S>) = NetMgr.getInstance().getRetrofit(baseUrl).create(service)

    fun clearCache(){
        NetMgr.getInstance().retrofitMap.clear()
        NetMgr.getInstance().clientMap.clear()
    }

    private fun getRetrofit(baseUrl: String, provider: NetProvider? = null) : Retrofit{
        if(checkBaseUrl(baseUrl)) throw  IllegalStateException("baseUrl error--> baseurl=$baseUrl")
        if(retrofitMap.get(baseUrl) != null) return retrofitMap.get(baseUrl)!!

        val tempProvider = provider?: providerMap.get(baseUrl)?: sProvider

        var gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

        var builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, tempProvider))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.build()
        retrofitMap[baseUrl] = retrofit
        providerMap[baseUrl] = tempProvider

        return retrofit
    }

    /**
     * 获取上传Service
     */
    private fun getRetrofitUpload(baseUrl: String): Retrofit{
        if (retrofitMap["upload-pic$baseUrl"] != null) {
            return retrofitMap["upload-pic$baseUrl"]!!
        }

        val provider: NetProvider = providerMap[baseUrl]?: sProvider

        val builder = OkHttpClient.Builder()
        val client = builder.build()

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

        val cookieJar = provider.configCookie()
        if (cookieJar != null) builder.cookieJar(cookieJar)

        provider.configHttps(builder)

        val handler = provider.configHandler()
        if (handler != null) {
            builder.addInterceptor(NetInterceptor(handler))
        }

        val interceptors = provider.configInterceptors()
        if (interceptors != null) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        val retBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = retBuilder.build()
        retrofitMap["upload-pic$baseUrl"] = retrofit

        return retrofit
    }

    private fun getClient(baseUrl: String, provider: NetProvider): OkHttpClient{
        if(checkBaseUrl(baseUrl)) throw  IllegalStateException("baseUrl error--> baseurl=$baseUrl")

        if(clientMap.get(baseUrl) != null) return clientMap.get(baseUrl)!!

        val builder = OkHttpClient.Builder()

        builder.connectTimeout(provider.configConnectTimeoutSecs(), TimeUnit.SECONDS)
        builder.readTimeout(provider.configReadTimeoutSecs(), TimeUnit.SECONDS)
        builder.writeTimeout(provider.configWriteTimeoutSecs(), TimeUnit.SECONDS)

        val cookieJar = provider.configCookie()
        if(cookieJar != null) builder.cookieJar(cookieJar)

        provider.configHttps(builder)

        val handler = provider.configHandler()
        if (handler != null) {
            builder.addInterceptor(NetInterceptor(handler))
        }

        val interceptors = provider.configInterceptors()
        if (interceptors != null) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        if (provider.configLogEnable()) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLogger())
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        val client = builder.build()
        clientMap[baseUrl] = client

        return client
    }

    private fun checkBaseUrl(baseUrl: String) = TextUtils.isEmpty(baseUrl)

    fun handleError(e: Throwable): String? {
        return when(e){
            is UnknownHostException -> "网络不可用"
            is SocketTimeoutException -> "请求网络超时"
            is SocketException, is SSLException -> "网络不可用"
            is HttpException -> when(e.code()){
                in 500 until 600 -> "服务器处理请求出错"
                in 400 until 500 -> "服务器无法处理请求"
                in 300 until 400 -> "请求被重定向到其他页面"
                else -> "未知错误${e.message}"
            }
            is ParseException, is JSONException -> "数据解析错误"
            else -> "未知错误${e.message}"
        }
    }


    companion object {//单例
        private val INSTANCE = NetMgr()
        fun getInstance() = INSTANCE
    }
}