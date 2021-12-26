package com.fastdev.data.repository

import android.content.ContentValues
import com.baselib.helper.CommonCacheHelper
import com.fastdev.data.response.SourceBean
import com.fastdev.data.repository.base.BaseRepository
import com.fastdev.data.response.SourceResp
import com.fastdev.helper.UserCacheHelper
import com.fastdev.helper.getAccountNo
import com.fastdev.ui.activity.task.viewmodel.Quantity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class DbApiRepository @Inject constructor(){
    val pageSize = 10
    /**
     * 是否缓存任务到本地
     */
    fun isStartTask(taskId: String?) = UserCacheHelper.getInstance().isStartTask(taskId)

    /**
     * 存储资源新数据
     */
    fun syncSave(taskId: String, data: SourceResp?): Boolean{
        if(data == null) return false
        val sourceList = data.property_list?.map { it.apply { task_id = taskId } } ?: return false
        if(data.area_list == null) return false
        val result = LitePal.runInTransaction {
            deleteTask(taskId) && LitePal.saveAll(sourceList) &&
            UserCacheHelper.getInstance().saveTask(taskId, data.detail) &&
            UserCacheHelper.getInstance().saveTaskPlace(taskId, data.area_list)
        }
        if(!result){
            UserCacheHelper.getInstance().deleteTask(taskId)
        }
        return result
    }

    /**
     * 更新扫描资源数据，根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun syncSaveOrUpdate(taskId: String, source: SourceBean?){
        if(source == null) return
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
     * 更新扫描资源数据，根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun syncSaveOrUpdate(taskId: String, beans: List<SourceBean>){
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

    fun update(taskId: String?, source: SourceBean?): Flowable<Boolean>{
        return Flowable.unsafeCreate<Boolean> {
            val result = LitePal.update(SourceBean::class.java, ContentValues().apply {
                put("pp_act", source?.pp_act)
                put("memo", source?.memo)
            }, source?.id?: -1)

            it.onNext(result > 0)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun queryStatusQuantity(taskId: String?): Flowable<Quantity>{
        return Flowable.unsafeCreate<Quantity>{
            it.onNext(Quantity(
                    LitePal.where("task_id = ?", taskId).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_WAIT).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_FINISH).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PY).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PK).count(SourceBean::class.java)
            ))
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByPY(taskId: String?, pageNum: Int): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data = LitePal.where("task_id = ? AND pp_act = ? LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}", taskId, SourceBean.STATUS_PY).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByFinish(taskId: String?, pageNum: Int): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data = LitePal.where("task_id = ? AND pp_act = ? LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}", taskId, SourceBean.STATUS_FINISH).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByPK(taskId: String?, pageNum: Int): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data = LitePal.where("task_id = ? AND pp_act = ? LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}", taskId, SourceBean.STATUS_PK).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByWait(taskId: String?, pageNum: Int): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data = LitePal.where("task_id = ? AND pp_act = ? LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}", taskId, SourceBean.STATUS_WAIT).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceAll(taskId: String?, pageNum: Int): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data = LitePal.where("task_id = ? LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}", taskId).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

//    fun queryTaskPyNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PY).find(SourceBean::class.java)?.size?: 0
//    fun queryTaskPkNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_PK).find(SourceBean::class.java)?.size?: 0
//    fun queryTaskWaitNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_WAIT).find(SourceBean::class.java)?.size?: 0
//    fun queryTaskFinishNum(taskId: String?): Int = LitePal.where("task_id = ? AND pp_act = ?", taskId, SourceBean.STATUS_FINISH).find(SourceBean::class.java)?.size?: 0
}