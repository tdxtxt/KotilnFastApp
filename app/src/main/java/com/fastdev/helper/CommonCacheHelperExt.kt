package com.fastdev.helper

import com.baselib.helper.CommonCacheHelper

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/7
 */

fun CommonCacheHelper.getGuideVersion(): String? = CommonCacheHelper.getString("guideVersion", "")
fun CommonCacheHelper.putGuideVersion(versionCode: String) = CommonCacheHelper.putString("guideVersion", versionCode)