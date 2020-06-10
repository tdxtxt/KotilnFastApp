package com.fastdev.app

import android.app.Application
import android.content.res.Configuration
import com.baselib.app.DevApp
import com.baselib.helper.ActStackHelper
import com.baselib.net.NetMgr
import com.fastdev.net.config.TonNetProvider

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 */
class CustomApp : DevApp() {
    override fun isLoggable() = true

    override fun onCreate() {
        super.onCreate()
        if (ActStackHelper.isMainProcess(this)) {
            //设置Host
            settingHost()
            //设置网络框架
            NetMgr.getInstance().registerProvider(TonNetProvider())
            //设置字体不跟随系统变化
            setTextSize()
            //设置分享
//            SocialHelper.initSDK(this)
        }
    }

    private fun setTextSize() {
        val config = Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun settingHost() {

    }

}