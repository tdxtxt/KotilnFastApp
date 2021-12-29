package com.baselib.rx.event

abstract class RxEventCode constructor(val code: Int, val data: Any? = null) {
    override fun equals(other: Any?): Boolean {
        return if (other is RxEventCode) code == other.code else false
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
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