package com.baselib.helper

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import java.util.Collections
import java.util.LinkedList

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 */
class ActStackHelper {
  val mActs: MutableList<Activity> = Collections.synchronizedList(LinkedList())

  /**
   * @param activity 作用说明 ：添加一个activity到管理里
   */
  fun addActivity(activity: Activity?) {
    activity?.apply { mActs.add(this) }
  }

  /**
   * @param activity 作用说明 ：删除一个activity在管理里
   */
  fun removeActivity(activity: Activity?) {
    activity?.apply { mActs.remove(this) }
  }

  /**
   * get current Activity 获取当前Activity（栈中最后一个压入的）
   */
  fun currentActivity(): Activity? {
    return if (mActs == null || mActs.isEmpty()) {
      null
    } else mActs[mActs.size - 1]
  }

  /**
   * 结束当前Activity（栈中最后一个压入的）
   */
  fun finishCurrentActivity() {
    if (mActs == null || mActs.isEmpty()) {
      return
    }
    val activity = mActs[mActs.size - 1]
    finishActivity(activity)
  }

  /**
   * 结束指定的Activity
   */
  fun finishActivity(activity: Activity?) {
    var activity = activity
    if (mActs == null || mActs.isEmpty()) {
      return
    }
    if (activity != null) {
      mActs.remove(activity)
      activity.finish()
    }
  }

  /**
   * 结束指定类名的Activity，栈中所有相同的activity都会清除掉
   */
  fun finishActivity(cls: Class<*>) {
    if (mActs == null || mActs.isEmpty()) {
      return
    }

    val iter = mActs.iterator()
    while (iter.hasNext()) {
      val activity = iter.next()
      if (activity.javaClass == cls) {
        iter.remove()
        activity.finish()
      }
    }
  }

  /**
   * 结束所有Activity
   */
  fun finishAllActivity() {
    if (mActs == null) {
      return
    }
    for (activity in mActs) {
      activity.finish()
    }
    mActs.clear()
  }

  /**
   * 结束该包名下所有activity,除传进来的activitys
   */
  fun finishPackageNameActivity(packageName: String, vararg keepActs: Class<Activity>?) {
    for (activity in mActs) {
      if (activity.javaClass.name == packageName && !keepActs?.contains(activity.javaClass)) {
        activity.finish()
      }
    }
  }

  /**
   * 按照指定类名找到activity
   * @param cls
   * @return
   */
  fun findActivity(cls: Class<*>): Activity? {
    var targetActivity: Activity? = null
    if (mActs != null) {
      for (activity in mActs) {
        if (activity.javaClass == cls) {
          targetActivity = activity
          break
        }
      }
    }
    return targetActivity
  }

  /**
   * @return 作用说明 ：获取当前最顶部activity的实例
   */
  fun getTopActivity(): Activity? {
    var mBaseActivity: Activity? = null
    synchronized(mActs) {
      val size = mActs.size - 1
      if (size < 0) {
        return null
      }
      mBaseActivity = mActs[size]
    }
    return mBaseActivity
  }

  fun isExistActivity(activityClass: Class<*>?): Boolean {
    if (activityClass == null) return false
    if (mActs == null) return false
    for (activity in mActs) {
      if (activity.javaClass.simpleName == activityClass.simpleName) {
        return true
      }
    }

    return false
  }

  companion object {
    val INSTANCE = ActStackHelper()

    /**
     * 退出应用程序
     */
    fun appExit() {
      try {
        ActStackHelper.INSTANCE.finishAllActivity()
        android.os.Process.killProcess(android.os.Process.myPid())
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    fun isMainProcess(app: Application): Boolean {
      val am = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      val processInfos = am.runningAppProcesses
      val mainProcessName = app.packageName
      val myPid = android.os.Process.myPid()
      for (info in processInfos) {
        if (info.pid == myPid && mainProcessName == info.processName) {
          return true
        }
      }
      return false
    }

  }
}