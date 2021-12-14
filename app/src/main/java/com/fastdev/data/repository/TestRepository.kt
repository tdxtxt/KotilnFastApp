package com.fastdev.data.repository

import com.fastdev.data.ResponseBody
import com.fastdev.data.db.Source
import com.fastdev.data.repository.api.NetApi
import com.fastdev.net.ApiClient
import com.fastdev.data.repository.base.BaseRepository
import io.reactivex.Flowable
import org.litepal.LitePal
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/7
 */
class TestRepository @Inject constructor() : BaseRepository() {
    private val netApi: NetApi = ApiClient.getNetApi()

    fun login(): Flowable<ResponseBody<String>>{
        return netApi.login()
    }

    fun saveAssets(bean: Source){
        bean.save()
    }

    fun saveListAssets(beans: List<Source>){
        LitePal.saveAll(beans)
    }

    fun query() = LitePal.findFirst(Source::class.java)
}