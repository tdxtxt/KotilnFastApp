package com.baselib.helper

import android.Manifest
import android.support.v4.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

/**
 * 功能描述:权限申请工具类
 * @author tangdexiang
 * @since 2020.5.27
 */
object RequestPermissionHelper{
    fun requestPermission(activity: FragmentActivity, vararg permissions: String, listener: (PermissionListener.() -> Unit)){
        val callback = object : PermissionListener() {}
        callback.listener()
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason { deniedList ->
                    showRequestReasonDialog(deniedList, "摄像机权限是程序必须依赖的权限", "明白", "取消")
                }
                .onForwardToSettings { deniedList ->
                    showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "明白", "取消")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {//允许的权限 grantedList
                        callback.onGranted?.invoke(grantedList)
                    } else {//被拒绝的权限 deniedList
                        callback.onDenied?.invoke(deniedList)
                    }
                }
    }
    /**
     * 请求相机权限
     */
    fun requestCameraPermission(activity: FragmentActivity, listener: (PermissionListener.() -> Unit)){
        val callback = object : PermissionListener() {}
        callback.listener()
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason { deniedList ->
                    showRequestReasonDialog(deniedList, "摄像机权限是程序必须依赖的权限", "明白", "取消")
                }
                .onForwardToSettings { deniedList ->
                    showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "明白", "取消")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {//允许的权限 grantedList
                        callback.onGranted?.invoke(grantedList)
                    } else {//被拒绝的权限 deniedList
                        callback.onDenied?.invoke(deniedList)
                    }
                }
    }

    /**
     * 请求存储权限
     */
    fun requestStoragePermission(activity: FragmentActivity, listener: (PermissionListener.() -> Unit)){
        val callback = object : PermissionListener() {}
        callback.listener()
        PermissionX.init(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason { deniedList ->
                    showRequestReasonDialog(deniedList, "存储权限是程序必须依赖的权限", "明白", "取消")
                }
                .onForwardToSettings { deniedList ->
                    showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "明白", "取消")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {//允许的权限 grantedList
                        callback.onGranted?.invoke(grantedList)
                    } else {//被拒绝的权限 deniedList
                        callback.onDenied?.invoke(deniedList)
                    }
                }
    }
}

abstract class PermissionListener{
    var onGranted: ((grantedList: List<String>?) -> Unit)? = null
    var onDenied: ((deniedList: List<String>?) -> Unit)? = null


    fun grantedPermission(granted: (grantedList: List<String>?) -> Unit){
        onGranted = granted
    }
    fun deniedPermission(denied: (deniedList: List<String>?) -> Unit){
        onDenied = denied
    }
}