package com.fastdev.helper

import com.baselib.helper.cache.CEngine
import com.baselib.helper.cache.MMKVEngine

class TaskDao constructor(taskName: String) : CEngine by object : MMKVEngine(){
    override fun createMMKVFileKey() = taskName
}{

    companion object{
        private val map = HashMap<String, TaskDao?>()
        fun getInstance(taskName: String): TaskDao{
            var instance = map[taskName]
            if(instance == null){
                instance = TaskDao(taskName)
                map[taskName] = instance
            }
            return instance
        }
    }
}