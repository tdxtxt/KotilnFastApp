package com.baselib.app

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 */
interface ForegroundListener {
    /**
     * 切换到前台时调用
     */
    fun onBecameForeground()
    /**
     * 切换到后台时调用
     */
    fun onBecameBackground()
}