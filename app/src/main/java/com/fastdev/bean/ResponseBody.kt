package com.fastdev.bean

import com.baselib.net.model.IModel

class ResponseBody<T> : IModel{
    var status: String? = null
    var data: T? = null
    var msg: String? = null

    override fun isSuccess() = "0".equals(status)

    override fun getMessage() = msg
}