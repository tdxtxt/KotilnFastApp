package com.fastdev.net

import com.baselib.net.NetMgr
import com.fastdev.Flavor
import com.fastdev.data.repository.api.NetApi

object ApiClient {
    private var HOST1 = Flavor.createBaseUrl()

    fun getNetApi() =  NetMgr.getInstance().get(HOST1, NetApi::class.java)

    fun changeHost(host: String){
        HOST1 = host
    }

    fun getHost1() = HOST1
}