package com.fastdev.core

import com.baselib.helper.ToastHelper
import com.seuic.uhf.EPC
import com.seuic.uhf.UHFService

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
object UHFSdk {
    private val device: UHFService? = try{ UHFService.getInstance() } catch (e: Exception){ null }

    fun getPower() = device?.power?: 0
    fun setPower(value: Int) = device?.setPower(value)

    fun start(){
        //开始寻卡是否清空之前EPC
//        device?.setParameters(UHFService.PARAMETER_CLEAR_EPCLIST_WHEN_START_INVENTORY, 1)
        device?.inventoryStart()
    }

    fun stop(){
        device?.inventoryStop()
    }
    fun read(): MutableList<EPC>?{
        return device?.tagIDs
    }

    fun syncOpen() = device?.open()?: false
    fun syncClose() = device?.close()?: false

    fun resume(){
        val ret = syncOpen()
        if(ret == false) ToastHelper.showToast("设备打开失败")
    }

    fun pause(){
        syncClose()
    }

}