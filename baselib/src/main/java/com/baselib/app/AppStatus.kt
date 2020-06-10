package com.baselib.app

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/6/4
 */
class AppStatusManager {
    private var appStatus = AppStatusConstant.STATUS_FORCE_KILLED //APP状态初始值为没启动不在前台状态

    fun getAppStatus() = appStatus
    fun setAppStatus(appStatus: Int){
        this.appStatus = appStatus
    }
    companion object {
        val INSTANCE by lazy { AppStatusManager() }
    }
}

object AppStatusConstant{
    val STATUS_FORCE_KILLED = -1 //应用放在后台被强杀了
    val STATUS_NORMAL = 2 //APP正常态//intent到MainActivity区分跳转目的
    val RESTART_ACTION = "restart_action" //重启
}