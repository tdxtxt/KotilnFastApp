package com.baselib.helper.cache

import android.content.Context
import android.os.Parcelable
import com.baselib.helper.LogA
import com.tencent.mmkv.MMKV

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/11
 */
object MMKVEngine : CEngine {
    override fun init(context: Context) {
        val rootDir = MMKV.initialize(context.filesDir.absolutePath + "/mmkv");
        LogA.i("mmkv root: $rootDir");
    }

    override fun putString(key: String, value: String?) = MMKV.defaultMMKV().encode(key, value)

    override fun putInt(key: String, value: Int) = MMKV.defaultMMKV().encode(key, value)

    override fun putFloat(key: String, value: Float) = MMKV.defaultMMKV().encode(key, value)

    override fun putDouble(key: String, value: Double) = MMKV.defaultMMKV().encode(key, value)

    override fun putBoolean(key: String, value: Boolean) = MMKV.defaultMMKV().encode(key, value)

    override fun <T> put(key: String, value: Parcelable?) = MMKV.defaultMMKV().encode(key, value)

    override fun <T> put(key: String, data: T?) = false

    override fun getString(key: String, defaultValue: String) = MMKV.defaultMMKV().decodeString(key, defaultValue)

    override fun getInt(key: String, defaultValue: Int) = MMKV.defaultMMKV().decodeInt(key, defaultValue)

    override fun getFloat(key: String, defaultValue: Float)  = MMKV.defaultMMKV().decodeFloat(key, defaultValue)

    override fun getDouble(key: String, defaultValue: Double) = MMKV.defaultMMKV().decodeDouble(key, defaultValue)

    override fun getBoolean(key: String, defaultValue: Boolean) = MMKV.defaultMMKV().decodeBool(key, defaultValue)

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultVale: T?) = MMKV.defaultMMKV().decodeParcelable(key, clazz, defaultVale)

    override fun <T> get(key: String) = null

    override fun removeAll() = MMKV.defaultMMKV().clearAll().run { true }

    override fun remove(key: String) = MMKV.defaultMMKV().remove(key).run { true }
}