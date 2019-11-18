package com.baselib.helper

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * @作者： ton
 * @创建时间： 2018\5\16 0016
 * @功能描述：日志打印工具类
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
class LogA {
    companion object {
        private var isLog = true

        fun init(isLog : Boolean){
            LogA.isLog = isLog
            init()
        }

        fun init(){
            Logger.clearLogAdapters()
            var formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  //是否显示线程信息，默认 显示
                    .methodCount(0)         //中间的方法栈打印的行数，默认是 2
                    .methodOffset(7)        //设置调用堆栈的函数偏移值，默认是 5
                    //.logStrategy(customLog)   //设置log打印策略，默认是 LogCat
                    .tag("application-ton")// 设置全局TAG，默认是 PRETTY_LOGGER
                    .build();
            Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    //根据返回的boolean值来控制是否打印log
                    return isLog
                }
            })
        }

        fun d(message: String?) = if(isLog) Logger.d(message) else Unit
        fun i(message: String?) = if(isLog) Logger.i(message?: "") else Unit
        fun w(message: String?, e: Throwable?) = if(isLog) Logger.w("$message:${e?.toString()}") else Unit
        fun e(message: String?) = if(isLog) Logger.e(message?: "") else Unit
        fun json(json: String) = if (isLog) Logger.json(json) else Unit
    }
}