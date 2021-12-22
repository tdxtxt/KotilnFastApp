package com.fastdev.data

import com.baselib.net.model.IModel
import com.google.gson.annotations.SerializedName

//@Parcelize
data class ResponseBody<T> (
        val status: String? = null,
        @SerializedName("message")
        val msg: String? = null
) : IModel/*, Parcelable*/ {
    val data: ResultData<T>? = null

    override fun isSuccess() = "1".equals(status)

    override fun getMessage() = msg
}
data class ResultData<T>(val rows: List<T>?){
    fun getData() = rows?.firstOrNull()
    fun getListData() = rows
    fun isEmpty() = rows?.firstOrNull() == null
}

data class BaseListBody<T>(val list: MutableList<T>?, val isNextPage: Boolean)