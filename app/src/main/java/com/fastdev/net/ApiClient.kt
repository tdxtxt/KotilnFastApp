package com.fastdev.net

import com.baselib.net.NetMgr
import com.fastdev.Flavor
import com.fastdev.data.repository.api.TestApi
import com.fastdev.data.repository.api.UserApi

object ApiClient {
    private var HOST1 = Flavor.createBaseUrl()

    fun getTestApi() =  NetMgr.getInstance().get(HOST1, TestApi::class.java)

    fun getUserApi() = NetMgr.getInstance().get(HOST1, UserApi::class.java)

    fun changeHost(host: String){
        HOST1 = host
    }

    fun getHost1() = HOST1
}