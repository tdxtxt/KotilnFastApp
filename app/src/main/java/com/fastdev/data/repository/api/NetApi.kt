package com.fastdev.data.repository.api

import com.fastdev.data.ResponseBody
import com.fastdev.data.request.SourceRequest
import com.fastdev.data.response.LoginEntity
import com.fastdev.data.response.SourceBean
import com.fastdev.data.response.SourceResp
import com.fastdev.data.response.TaskEntity
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetApi {
    @POST("api/pd/public/login.do")
    fun login(@Body params: @JvmSuppressWildcards Map<String, Any>): Flowable<ResponseBody<LoginEntity>>

    @GET("api/pd/getTaskList.do")
    fun queryTaskList(@Query("type") type: String, @Query("page") pageNum: Int, @Query("rows") pageSize: Int): Flowable<ResponseBody<TaskEntity>>

    @GET("api/pd/getTaskData.do")
    fun queryAllSourceByTask(@Query("task_id") taskId: String?): Flowable<ResponseBody<SourceResp>>

    @GET("api/pd/getPropertyDetail.do")
    fun querySourceBycode(@Query("pp_code") code: String?): Flowable<ResponseBody<SourceBean>>

    @POST("api/pd/saveData.do")
    fun postAllSource(@Body parmas: SourceRequest): Flowable<ResponseBody<Any>>

    @GET("api/pd/getHousePropertyList.do")
    fun querySourceByRoom(@Query("house_code") code: String?, ): Call<ResponseBody<SourceBean>>
}