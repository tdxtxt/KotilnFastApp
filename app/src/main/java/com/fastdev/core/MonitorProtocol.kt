package com.fastdev.core

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.baselib.helper.ToastHelper
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import com.seuic.uhf.UHFService

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
abstract class MonitorProtocol constructor(val looper: Looper?) {
    companion object{
        private var monitorThread: HandlerThread? = null
        private var readMonitor: MonitorProtocol? = null

        fun startReadMonitor(viewModel: TaskDetailsViewModel){
            if(monitorThread != null) stopReadMonitor()
            monitorThread = HandlerThread("monitor_thread")
            monitorThread?.start()
            readMonitor = ReadTagMonitor(monitorThread?.looper, viewModel)
            readMonitor?.start()
        }
        fun stopReadMonitor(){
            readMonitor?.close()
            monitorThread?.quitSafely()
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
        next()
    }

    open fun close(){
        refreshHandler?.removeCallbacks(runnable)
        refreshHandler = null
    }

    fun next(){
        refreshHandler?.postDelayed(runnable, 1000L)
    }
}