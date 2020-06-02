package com.fastdev.net.host

import com.baselib.helper.HashMapParams
import com.fastdev.bean.ResponseBody
import io.reactivex.Flowable
import retrofit2.http.*

open interface Host1Api {
    //获取公众号列表
    @GET("wxarticle/chapters/json")
    fun testApi1(): Flowable<ResponseBody<String>>

    @FormUrlEncoded
    @POST("api/xxxyyy")
    fun testApi2(@Field("parmas") parmas: String): Flowable<ResponseBody<Any?>>
}