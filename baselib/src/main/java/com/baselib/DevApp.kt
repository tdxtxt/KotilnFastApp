package com.baselib

import android.app.Application
import android.content.Context
import com.baselib.helper.LogA

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 需在application中调用onCreate方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
open abstract class DevApp {
    fun onCreate(app: Application){
        DevApp.mContext = app
        LogA.init(isLoggable())

    }

    abstract fun isLoggable(): Boolean

    companion object {
        var mContext: Context ? = null
    }
}