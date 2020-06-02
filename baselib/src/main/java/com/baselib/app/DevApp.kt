package com.baselib.app

import android.app.Application
import android.content.Context
import com.baselib.helper.CacheHelper
import com.baselib.helper.LogA

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 需在application中调用onCreate方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
open abstract class DevApp : Application(){

    override fun onCreate() {
        super.onCreate()
        mContext = this
        LogA.init(isLoggable())
        CacheHelper.init()
        ForegroundCallbacks.init(this)
                .addListener(object : ForegroundListener{
                    override fun onBecameForeground() {
                    }
                    override fun onBecameBackground() {
                    }
                })
    }

    abstract fun isLoggable(): Boolean

    companion object {
        var mContext: Context ? = null
        fun getContext(): Context? {
            return mContext
        }
    }
}