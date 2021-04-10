package com.fastdev.data.repository

import com.fastdev.data.ResponseBody
import com.fastdev.data.repository.api.TestApi
import com.fastdev.net.ApiClient
import com.fastdev.data.repository.api.UserApi
import com.fastdev.data.repository.base.BaseRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/7
 */
class TestRepository @Inject constructor() : BaseRepository() {
    private val testApi: TestApi = ApiClient.getTestApi()

    fun queryList1(): Flowable<ResponseBody<String>>{
        return testApi.queryList1()
    }
}