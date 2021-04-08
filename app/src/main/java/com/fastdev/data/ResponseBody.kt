package com.fastdev.data

import android.os.Parcelable
import com.baselib.net.model.IModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseBody<T> (
        val status: String? = null,
        val msg: String? = null
) : IModel, Parcelable {
    val data: T? = null

    override fun isSuccess() = "0".equals(status)

    override fun getMessage() = msg
}