package com.fastdev.data.repository

import com.fastdev.data.response.SourceBean
import com.fastdev.data.repository.base.BaseRepository
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findFirstAsync
import javax.inject.Inject

/**
 * 功能描述: 数据库操作仓库
 * @author tangdexiang
 * @since 2021/12/20
 */
class DbApiRepository @Inject constructor() : BaseRepository() {
    fun saveSource(bean: SourceBean){
        bean.saveOrUpdate()
        bean.save()
    }

    fun saveOrUpdate(taskId: String, beans: List<SourceBean>){
        beans.forEach {
            it.task_id = taskId
            it.saveOrUpdate("task_id = ? AND pp_code = ?", taskId, it.pp_code)
        }
    }

    fun deleteTask(taskId: String?){
        LitePal.deleteAll(SourceBean::class.java, "task_id = ?", taskId)
    }


    fun queryTaskPyNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PY).find(SourceBean::class.java)?.size?: 0
    fun queryTaskPkNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PK).find(SourceBean::class.java)?.size?: 0
    fun queryTaskWaitNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_WAIT).find(SourceBean::class.java)?.size?: 0
    fun queryTaskFinishNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_FINISH).find(SourceBean::class.java)?.size?: 0
    fun isStartTask() = false
}