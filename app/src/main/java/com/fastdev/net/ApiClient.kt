package com.fastdev.net

import com.baselib.net.NetMgr
import com.fastdev.data.repository.api.Host1Api
import com.fastdev.data.repository.api.UserApi
import com.fastdev.ui.BuildConfig

class ApiClient {
    companion object {
        private var HOST1 = BuildConfig.HOST1

        fun getService() =  NetMgr.getInstance().get(HOST1, Host1Api::class.java)

        fun getUserApi() = NetMgr.getInstance().get(HOST1, UserApi::class.java)

        fun changeHost(host: String){
            HOST1 = host
        }

        fun getHost1() = HOST1
    }
}