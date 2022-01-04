package com.fastdev.helper

import com.baselib.helper.CommonCacheHelper
import com.baselib.helper.cache.CEngine
import com.baselib.helper.cache.MMKVEngine
import com.fastdev.data.response.PlaceBean
import com.fastdev.data.response.TaskEntity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class UserCacheHelper constructor(userName: String) : CEngine by object : MMKVEngine(){
    override fun createMMKVFileKey() = userName
}{
    private val gson = GsonBuilder().create()

    fun saveTaskPlace(taskId: String?, place: List<PlaceBean>?): Boolean{
        if(place == null) return true
        return putString("taskPlace_${taskId}", gson.toJson(place))?: false
    }

    fun deleteTaskPlace(taskId: String?): Boolean?{
        return remove("taskPlace_${taskId}")
    }

    fun getTaskPlace(taskId: String?): List<PlaceBean>?{
        val json = getString("taskPlace_${taskId}")
        if(json?.startsWith("[") == true){
            return gson.fromJson<List<PlaceBean>>(json, object : TypeToken<List<PlaceBean>>() {}.type)
        }
        return null
    }

    fun saveTask(taskId: String?, task: TaskEntity?): Boolean{
        return put<TaskEntity>("task_${taskId}", task)?: false
    }

    fun deleteTask(taskId: String?) : Boolean?{
        return remove("task_${taskId}")
    }

    fun getTask(taskId: String?): TaskEntity?{
        return getParcelable("task_${taskId}", TaskEntity::class.java)
    }

    fun getPowerValue(): Int = getInt("power_value", 30)?: 30
    fun savePowerValue(value: Int) = put("power_value", value)

    fun isStartTask(taskId: String?) = getTask(taskId) != null

    companion object{
        private val map = HashMap<String, UserCacheHelper?>()
        fun getInstance(userName: String = CommonCacheHelper.getAccountNo()?: ""): UserCacheHelper{
            var instance = map[userName]
            if(instance == null){
                instance = UserCacheHelper(userName)
                map[userName] = instance
            }
            return instance
        }
    }
}