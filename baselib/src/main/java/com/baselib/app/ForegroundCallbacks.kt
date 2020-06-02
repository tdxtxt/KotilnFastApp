package com.baselib.app

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.baselib.helper.ActStackHelper
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 */
class ForegroundCallbacks : Application.ActivityLifecycleCallbacks {

    companion object {
        val CHECK_DELAY = 500L
        val INSTANCE = ForegroundCallbacks()

        fun init(application: Application?): ForegroundCallbacks{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                application?.registerActivityLifecycleCallbacks(INSTANCE)
            }
            return INSTANCE
        }
    }

    private var foreground = false
    private var paused = true
    private var handler = Handler()
    private var listeners = CopyOnWriteArrayList<ForegroundListener>()
    private var check: Runnable? = null

    fun isForeground() = foreground
    fun isBackground() = !foreground
    fun addListener(listener: ForegroundListener){
        listeners.add(listener)
    }


    override fun onActivityPaused(activity: Activity?) {
        paused = true
        if(check != null) handler.removeCallbacks(check)
        check = Runnable {
            if(foreground and paused){
                foreground = false
                for(listener in listeners){
                    try{
                        listener.onBecameBackground()
                    }catch (e: Exception){

                    }
                }
            }
        }
        handler.postDelayed(check, ForegroundCallbacks.CHECK_DELAY)
    }

    override fun onActivityResumed(activity: Activity?) {
        paused = false
        var wasBackground = !foreground
        foreground = true
        if(check != null) handler.removeCallbacks(check)
        if(wasBackground){
            for(listener in listeners){
                try {
                    listener.onBecameForeground()
                }catch (e: Exception){

                }
            }
        }
    }
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        ActStackHelper.INSTANCE.addActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        ActStackHelper.INSTANCE.removeActivity(activity)
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }
}