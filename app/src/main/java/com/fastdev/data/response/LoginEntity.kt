package com.fastdev.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/20
 */
@Parcelize
data class LoginEntity(val passport: String, val token: String) : Parcelable