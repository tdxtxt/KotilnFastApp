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
abstract class MMKVEngine : CEngine {
    abstract fun createMMKVFileKey(): String
    
    override fun init(context: Context) {
        val rootDir = MMKV.initialize(context)
        LogA.i("mmkv root dir: $rootDir");
    }

    override fun putString(key: String, value: String?) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun putInt(key: String, value: Int) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun putFloat(key: String, value: Float) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun putDouble(key: String, value: Double) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun putBoolean(key: String, value: Boolean) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun <T> put(key: String, value: Parcelable?) = MMKV.mmkvWithID(createMMKVFileKey()).encode(key, value)

    override fun <T> put(key: String, data: T?) = false

    override fun getString(key: String, defaultValue: String) = MMKV.mmkvWithID(createMMKVFileKey()).decodeString(key, defaultValue)

    override fun getInt(key: String, defaultValue: Int) = MMKV.mmkvWithID(createMMKVFileKey()).decodeInt(key, defaultValue)

    override fun getFloat(key: String, defaultValue: Float)  = MMKV.mmkvWithID(createMMKVFileKey()).decodeFloat(key, defaultValue)

    override fun getDouble(key: String, defaultValue: Double) = MMKV.mmkvWithID(createMMKVFileKey()).decodeDouble(key, defaultValue)

    override fun getBoolean(key: String, defaultValue: Boolean) = MMKV.mmkvWithID(createMMKVFileKey()).decodeBool(key, defaultValue)

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultVale: T?) = MMKV.mmkvWithID(createMMKVFileKey()).decodeParcelable(key, clazz, defaultVale)

    override fun <T> get(key: String) = null

    override fun removeAll() = MMKV.mmkvWithID(createMMKVFileKey()).clearAll().run { true }

    override fun remove(key: String) = MMKV.mmkvWithID(createMMKVFileKey()).remove(key).run { true }
}