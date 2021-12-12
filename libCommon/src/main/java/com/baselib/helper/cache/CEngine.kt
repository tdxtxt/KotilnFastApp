package com.baselib.helper.cache

import android.content.Context
import android.os.Parcelable

/**
 * 创建时间： 2020/5/22
 * 编码： tangdex
 * 功能描述: 缓存工具类抽象方法
 */
interface CEngine {
    fun init(context: Context)

    fun putString(key: String, value: String?): Boolean?
    fun putInt(key: String, value: Int): Boolean?
    fun putFloat(key: String, value: Float): Boolean?
    fun putDouble(key: String, value: Double): Boolean?
    fun putBoolean(key: String, value: Boolean): Boolean?
    fun <T> put(key: String, data: Parcelable?): Boolean?
    fun <T> put(key: String, data: T?): Boolean?

    fun getString(key: String, defaultValue: String = ""): String?
    fun getInt(key: String, defaultValue: Int = 0): Int?
    fun getFloat(key: String, defaultValue: Float = 0F): Float?
    fun getDouble(key: String, defaultValue: Double = 0.0): Double?
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean?
    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultVale: T? = null): T?
    fun <T> get(key: String): T?

    fun removeAll(): Boolean?
    fun remove(key: String): Boolean?

}