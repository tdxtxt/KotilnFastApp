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

    fun start(){
        if(refreshHandler == null) refreshHandler = Handler(looper)
        close()
        next()
    }

    fun close(){
        refreshHandler?.removeCallbacks(runnable)
    }

    fun next(){
        refreshHandler?.postDelayed(runnable, 1000L)
    }
}