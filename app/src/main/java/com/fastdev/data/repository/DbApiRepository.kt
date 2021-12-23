package com.fastdev.data.repository

import com.fastdev.data.response.SourceBean
import com.fastdev.data.repository.base.BaseRepository
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findFirstAsync
import org.litepal.extension.runInTransaction
import javax.inject.Inject

/**
 * 功能描述: 数据库操作仓库
 * @author tangdexiang
 * @since 2021/12/20
 */
class DbApiRepository @Inject constructor() : BaseRepository() {
    /**
     * 是否缓存任务到本地
     */
    fun isStartTask() = false

    /**
     * 存储新数据
     */
    fun save(taskId: String, data: List<SourceBean>){
        LitePal.runInTransaction {
            deleteTask(taskId) && LitePal.saveAll(data)
        }
    }

    /**
     * 根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun saveOrUpdate(taskId: String, source: SourceBean){
        source.task_id = taskId
        val list = LitePal.where("task_id = ?", taskId).find(SourceBean::class.java)
        if(list.isEmpty()){
            source.pp_act = SourceBean.STATUS_PY
            source.save()
        }else{
            val existSourceBean = list.firstOrNull { source.pp_code == it.pp_code }
            if(existSourceBean == null){
                source.pp_act = SourceBean.STATUS_PY;
                source.save()
            }else{
                if(existSourceBean.pp_act == SourceBean.STATUS_WAIT){
                    existSourceBean.pp_act = SourceBean.STATUS_FINISH
                    existSourceBean.update(existSourceBean.id)
                }
            }
        }
//        source.saveOrUpdate("task_id = ? AND pp_code = ?", taskId, source.pp_code)
    }
    /**
     * 根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun saveOrUpdate(taskId: String, beans: List<SourceBean>){
        val list = LitePal.where("task_id = ?", taskId).find(SourceBean::class.java)
        if(list.isEmpty()){
            beans.forEach { it.pp_act == SourceBean.STATUS_PY }
            LitePal.saveAll(beans)
        }else{
            beans.forEach {bean ->
                val existSourceBean = list.firstOrNull { it.pp_code == bean.pp_code }
                if(existSourceBean == null){
                    bean.pp_act = SourceBean.STATUS_PY;
                    bean.save()
                }else{
                    if(existSourceBean.pp_act == SourceBean.STATUS_WAIT){
                        existSourceBean.pp_act = SourceBean.STATUS_FINISH
                        existSourceBean.update(existSourceBean.id)
                    }
                }
            }
        }
    }
    /**
     * 删除任务与本地相关的所有资源
     */
    fun deleteTask(taskId: String?): Boolean{
        return LitePal.deleteAll(SourceBean::class.java, "task_id = ?", taskId) >= 0
    }

    fun queryTaskPyNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PY).find(SourceBean::class.java)?.size?: 0
    fun queryTaskPkNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PK).find(SourceBean::class.java)?.size?: 0
    fun queryTaskWaitNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_WAIT).find(SourceBean::class.java)?.size?: 0
    fun queryTaskFinishNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_FINISH).find(SourceBean::class.java)?.size?: 0
}