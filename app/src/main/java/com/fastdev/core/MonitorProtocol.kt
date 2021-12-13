package com.fastdev.core

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.seuic.uhf.UHFService

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
abstract class MonitorProtocol constructor(val looper: Looper) {
    companion object{
        val mDevice: UHFService? = null//UHFService.getInstance()
        private val monitorThread = HandlerThread("monitor_thread")
        private val readMonitor = ReadTagMonitor(monitorThread.looper)

        fun startReadMonitor(){
            //开始寻卡是否清空之前EPC
            mDevice?.setParameters(UHFService.PARAMETER_CLEAR_EPCLIST_WHEN_START_INVENTORY, 1)
            readMonitor.start()
        }
        fun stopAllMonitor(){
            monitorThread.quitSafely()
        }

    }

    var refreshHandler: Handler? = null

    private val runnable  = Runnable {
        task()
        next()
    }

    abstract fun task()

    open fun start(){
        if(refreshHandler == null) refreshHandler = Handler(looper)
        close()
        next()
    }

    open fun close(){
        refreshHandler?.removeCallbacks(runnable)
    }

    fun next(){
        refreshHandler?.postDelayed(runnable, 1000L)
    }
}