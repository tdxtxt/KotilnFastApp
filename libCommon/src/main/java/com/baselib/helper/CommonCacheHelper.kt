package com.baselib.helper

import com.baselib.helper.cache.CEngine
import com.baselib.helper.cache.MMKVEngine

/**
 * 创建时间： 2020/5/22
 * 编码： tangdex
 * 功能描述:
 */
object CommonCacheHelper: CEngine by object : MMKVEngine(){
    override fun createMMKVFileKey() = "comm_cache_file"
}