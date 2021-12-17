package com.baselib.rx.event

abstract class RxEventCode constructor(val code: Int, val data: Any? = null) {
    override fun equals(other: Any?): Boolean {
        if(other is RxEventCode){
            return code == other.code
        }
        return false
    }
    companion object{
        //登录成功通知
        val LOGIN = object : RxEventCode(1){}
        //申请登录通知
        val LOGIN_REQUEST = object : RxEventCode(2){}
        //登出通知
        val LOGOUT= object : RxEventCode(3){}
        //网络切换通知
        val NETWORK_CHANGE = object : RxEventCode(4){}
    }
}