package com.baselib.net.model

interface IModel {
    //状态成功
    fun isSuccess(): Boolean
    //后台返回信息
    fun getMessage(): String?
}