package com.fastdev.net

import com.baselib.net.NetMgr
import com.fastdev.data.repository.api.TestApi
import com.fastdev.data.repository.api.UserApi
import com.fastdev.ui.BuildConfig

object ApiClient {
    private var HOST1 = BuildConfig.HOST1

    fun getTestApi() =  NetMgr.getInstance().get(HOST1, TestApi::class.java)

    fun getUserApi() = NetMgr.getInstance().get(HOST1, UserApi::class.java)

    fun changeHost(host: String){
        HOST1 = host
    }

    fun getHost1() = HOST1
}