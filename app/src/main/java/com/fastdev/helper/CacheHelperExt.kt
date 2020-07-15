package com.fastdev.helper

import com.baselib.helper.CacheHelper

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/7
 */

fun CacheHelper.getGuideVersion(): String = CacheHelper.getString("guideVersion", "")
fun CacheHelper.putGuideVersion(versionCode: String) = CacheHelper.putString("guideVersion", versionCode)