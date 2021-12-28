package com.fastdev.core

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.baselib.helper.ToastHelper
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import com.seuic.uhf.UHFService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
abstract class MonitorProtocol constructor(val looper: Looper?) {
    companion object{
        var isRun = false
        private var monitorThread: HandlerThread? = null
        private var readMonitor: MonitorProtocol? = null

        fun startReadMonitor(viewModel: TaskDetailsViewModel, dbApiRepository: DbApiRepository){
            if(isRun) return
            isRun = true
            monitorThread = HandlerThread("monitor_thread")
            monitorThread?.start()
            readMonitor = ReadTagMonitor(monitorThread?.looper, viewModel, dbApiRepository)
            readMonitor?.start()
        }
        fun stopReadMonitor(){
            isRun = false
            readMonitor?.close()
            monitorThread?.quitSafely()
            readMonitor = null
        }

        fun onResume(){
            Flowable.unsafeCreate<Unit> {
                UHFSdk.resume()
                it.onNext(Unit)
                it.onComplete()
            }.subscribeOn(Schedulers.io()).subscribe()
        }

        fun onPause(){
            stopReadMonitor()
            UHFSdk.pause()
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