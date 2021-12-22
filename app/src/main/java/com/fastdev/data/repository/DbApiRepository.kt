package com.fastdev.data.repository

import com.fastdev.data.response.SourceBean
import com.fastdev.data.repository.base.BaseRepository
import org.litepal.LitePal

/**
 * 功能描述: 数据库操作仓库
 * @author tangdexiang
 * @since 2021/12/20
 */
class DbApiRepository : BaseRepository() {
    fun saveSource(bean: SourceBean){
        bean.save()
    }

    fun saveSources(beans: List<SourceBean>){
        LitePal.saveAll(beans)
    }

    fun querySource() = LitePal.findFirst(SourceBean::class.java)

    fun queryTaskPyNumByTask(taskId: String?) = 0
    fun queryTaskPkNumByTask(taskId: String?) = 0
    fun queryTaskWaitNumByTask(taskId: String?) = 0
    fun queryCompletePkNumByTask(taskId: String?) = 0
    fun isStartTask() = false
}