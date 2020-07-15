package com.baselib.helper.cache

import com.baselib.app.DevApp
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.NoEncryption
import com.orhanobut.hawk.Storage

/**
 * 创建时间： 2020/5/22
 * 编码： tangdex
 * 功能描述:
 */
object HawkEngine : CEngine{
  override fun init() = Hawk.init(DevApp.mContext).build()

  override fun putString(key: String, value: String?) = Hawk.put(key, value)

  override fun putInt(key: String, value: Int) = Hawk.put(key, value)

  override fun putFloat(key: String, value: Float) = Hawk.put(key, value)

  override fun putDouble(key: String, value: Double) = Hawk.put(key, value)

  override fun putBoolean(key: String, value: Boolean) = Hawk.put(key, value)

  override fun <T> put(key: String, data: T?) = Hawk.put(key, data)

  override fun getString(key: String, defaultValue: String) = Hawk.get(key, defaultValue)

  override fun getInt(key: String, defaultValue: Int) = Hawk.get(key, defaultValue)

  override fun getFloat(key: String, defaultValue: Float) = Hawk.get(key, defaultValue)

  override fun getDouble(key: String, defaultValue: Double) = Hawk.get(key, defaultValue)

  override fun getBoolean(key: String, defaultValue: Boolean) = Hawk.get(key, defaultValue)

  override fun <T> get(key: String): T? {
      return Hawk.get(key)
  }

  override fun removeAll() = Hawk.deleteAll()

  override fun remove(key: String) = Hawk.delete(key)

}