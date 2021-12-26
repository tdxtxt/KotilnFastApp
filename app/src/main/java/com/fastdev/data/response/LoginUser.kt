package com.fastdev.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/26
 */
@Parcelize
data class LoginUser (val userName: String?, val pwd: String?): Parcelable