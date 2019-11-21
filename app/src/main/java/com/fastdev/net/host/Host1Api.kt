package com.fastdev.net.host

import com.baselib.helper.HashMapParams
import com.fastdev.bean.ResponseBody
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Host1Api {
    @FormUrlEncoded
    @POST("api/xxx")
    fun testApi1(@FieldMap params: HashMapParams): Flowable<ResponseBody<String>>

    @FormUrlEncoded
    @POST("api/xxxyyy")
    fun testApi2(@Field("parmas") parmas: String): Flowable<ResponseBody<Any?>>
}