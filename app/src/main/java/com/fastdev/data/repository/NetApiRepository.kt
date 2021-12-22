package com.fastdev.data.repository

import com.fastdev.data.ResponseBody
import com.fastdev.data.repository.api.NetApi
import com.fastdev.net.ApiClient
import com.fastdev.data.repository.base.BaseRepository
import com.fastdev.data.response.LoginEntity
import com.fastdev.data.response.SourceResp
import com.fastdev.data.response.TaskEntity
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * 功能描述: 网络操作仓库
 * @author tangdexiang
 * @since 2021/4/7
 */
class NetApiRepository @Inject constructor() : BaseRepository() {
    private val netApi: NetApi = ApiClient.getNetApi()

    fun login(accout: String, pwd: String): Flowable<ResponseBody<LoginEntity>>{
        return netApi.login(HashMap<String, Any>().apply {
            put("app_id", "pd")
            put("passport", accout)
            put("pwd", pwd)
        })
    }

    fun queryTaskList(type: String, pageNum: Int, pageSize: Int): Flowable<ResponseBody<TaskEntity>>{
        return netApi.queryTaskList(type, pageNum, pageSize)
    }

    fun queryAllSourceByTask(taskId: String?): Flowable<ResponseBody<SourceResp>>{
        return netApi.queryAllSourceByTask(taskId)
    }
}