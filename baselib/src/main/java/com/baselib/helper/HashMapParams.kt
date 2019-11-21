package com.baselib.helper

import java.lang.Exception

class HashMapParams : HashMap<String,Any>(){
    fun add(key: String, value: Any) = this.put(key, value).run { this@HashMapParams }

    fun <T> get(key: String, defaultValue: T? = null): T?{
        try {
            var v: T? = get(key) as T?
            return v?: defaultValue
        }catch (e: Exception){}
        return defaultValue
    }

    fun getValue2String(key: String) = get(key)?.toString()

    fun copyFromMap(parmas: HashMap<String,Any>): HashMapParams{
        if(parmas == null) return this
        for (entry in parmas.entries){
            add(entry.key, entry.value)
        }
        return this
    }

}