package com.fastdev.helper

import android.text.TextUtils
import com.baselib.helper.CommonCacheHelper

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/7
 */

fun CommonCacheHelper.getGuideVersion(): String? = CommonCacheHelper.getString("guideVersion", "")
fun CommonCacheHelper.putGuideVersion(versionCode: String) = CommonCacheHelper.putString("guideVersion", versionCode)

fun CommonCacheHelper.isLogin(): Boolean = !TextUtils.isEmpty(getUserId())
fun CommonCacheHelper.putUserId(userId: String) = CommonCacheHelper.putString("userId", userId)
fun CommonCacheHelper.getUserId() = CommonCacheHelper.getString("userId", "")