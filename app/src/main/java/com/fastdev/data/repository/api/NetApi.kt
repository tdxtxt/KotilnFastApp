package com.fastdev.data.repository.api

import com.fastdev.data.ResponseBody
import com.fastdev.data.response.LoginEntity
import com.fastdev.data.response.SourceResp
import com.fastdev.data.response.TaskEntity
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
interface NetApi {
    @POST("api/public/login.do")
    fun login(@Body params: Map<String, Any>): Flowable<ResponseBody<LoginEntity>>

    @GET("api/pd/getTaskList.do")
    fun queryTaskList(@Query("type") type: String, @Query("page") pageNum: Int, @Query("rows") pageSize: Int): Flowable<ResponseBody<TaskEntity>>

    @GET("api/pd/getTaskData.do")
    fun queryAllSourceByTask(@Query("task_id") taskId: String?): Flowable<ResponseBody<SourceResp>>

}