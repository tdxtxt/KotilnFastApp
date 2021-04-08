package com.fastdev.data.repository.api

import com.fastdev.data.ResponseBody
import io.reactivex.Flowable
import retrofit2.http.*

/**
    方法名自定义
    @GET("接口地址")
    fun load():Observable<对应实体类>

    @FormUrlEncoded
    @Post("接口地址")
    fun load():Observable<对应实体类>

    需要传递参数，多个参数逗号隔开
    @GET("接口地址")
    fun load(@Query("参数名字") 参数名字（可自定义）:参数类型):Observable<对应实体类>

    @FormUrlEncoded
    @Post("接口地址")
    fun load(@Field("参数名字") 参数名字（可自定义）:参数类型):Observable<对应实体类>

    示例
    @GET("recipientOrg/findOrg")
    fun load():Observable<NetOuter<Orgs>>

    @GET("recipientOrg/findOrg")
    fun load(@Query("id") id:Int):Observable<NetOuter<Orgs>>
 */
open interface Host1Api {
    //获取公众号列表
    @GET("wxarticle/chapters/json")
    fun testApi1(): Flowable<ResponseBody<String>>

    @FormUrlEncoded
    @POST("api/xxxyyy")
    fun testApi2(@Field("parmas") parmas: String): Flowable<ResponseBody<Any?>>


}