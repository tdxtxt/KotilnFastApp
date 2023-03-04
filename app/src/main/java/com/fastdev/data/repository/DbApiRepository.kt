package com.fastdev.data.repository

import android.content.ContentValues
import android.text.TextUtils
import com.baselib.helper.LogA
import com.fastdev.data.response.SourceBean
import com.fastdev.data.response.SourceResp
import com.fastdev.helper.UserCacheHelper
import com.fastdev.ui.activity.task.viewmodel.Quantity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
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
        val sourceList = data.property_list?.map { it.apply { task_id = taskId; from_where = SourceBean.FROMWHERE_LOAD } } ?: return false
        if(data.area_list == null) return false

        val result = LitePal.runInTransaction {
            syncDeleteCacheByTask(taskId) &&
            LitePal.saveAll(sourceList) &&
            UserCacheHelper.getInstance().saveTask(taskId, data.detail) &&
            UserCacheHelper.getInstance().saveTaskPlace(taskId, data.area_list)
        }
        if(!result){
            UserCacheHelper.getInstance().deleteTask(taskId)
            UserCacheHelper.getInstance().deleteTaskPlace(taskId)
        }
        return result
    }

    fun queryPlaceList(taskId: String?) = UserCacheHelper.getInstance().getTaskPlace(taskId)

    /**
     * 更新扫描资源数据，根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun syncSaveOrUpdate(taskId: String, source: SourceBean?): SourceBean?{
        if(source == null) return null
        source.task_id = taskId
        val existSourceBean = LitePal.where("task_id = ? And pp_code = ?", taskId, source.pp_code).findFirst(SourceBean::class.java)
        if(existSourceBean == null){
            source.pp_act = SourceBean.STATUS_PY
            val b = source.save()
            LogA.e("资产${source.pp_code}新增：$b (状态${source.pp_act})")
            return source
        }else{
            if(existSourceBean.pp_act == SourceBean.STATUS_WAIT){
                existSourceBean.pp_act = SourceBean.STATUS_FINISH
                val b = existSourceBean.update(existSourceBean.id)
                LogA.e("资产${existSourceBean.pp_code}更新：$b (状态${existSourceBean.pp_act})")
            }
            return existSourceBean
        }
    }

    fun saveOrUpdate(taskId: String, source: SourceBean?): Flowable<SourceBean?>{
        return Flowable.unsafeCreate<SourceBean?> {
            it.onNext(syncSaveOrUpdate(taskId, source))
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 更新扫描资源数据，根据任务查询所有资产，若存在则根据状态来更新,不存在则存储并更新状态
     */
    fun syncSaveOrUpdate(taskId: String, beans: List<SourceBean>?){
        if(beans == null) return
        val list = LitePal.where("task_id = ?", taskId).find(SourceBean::class.java)
        if(list.isEmpty()){
            beans.forEach { it.pp_act == SourceBean.STATUS_PY }
            LitePal.saveAll(beans)
        }else{
            beans.forEach {bean ->
                val existSourceBean = list.firstOrNull { it.pp_code == bean.pp_code }
                if(existSourceBean == null){
                    bean.pp_act = SourceBean.STATUS_PY
                    val b = bean.save()
                    LogA.e("资产${bean.pp_code}新增：$b (状态${bean.pp_act})")
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
    fun syncDeleteCacheByTask(taskId: String?): Boolean{
        //删除数据库相关记录
        val result1 = LitePal.deleteAll(SourceBean::class.java, "task_id = ?", taskId)
        //删除mmkv相关记录
        val result2 = UserCacheHelper.getInstance().deleteTask(taskId)
        val result3 = UserCacheHelper.getInstance().deleteTaskPlace(taskId)
        return result1 >= 0
    }

    fun deleteCacheByTask(taskId: String?): Flowable<Boolean>{
        return Flowable.unsafeCreate<Boolean> {
            it.onNext(syncDeleteCacheByTask(taskId))
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 删除资源
     */
    fun syncDeleteSourceByTask(taskId: String?, source: SourceBean?){
        //查询本地存储的资源
        val existSourceBean = LitePal.where("task_id = ? AND pp_code = ?", taskId, source?.pp_code).findFirst(SourceBean::class.java)
                ?: return

        if(existSourceBean.from_where == SourceBean.FROMWHERE_LOAD){
            val b = LitePal.update(SourceBean::class.java, ContentValues().apply {
                put("pp_act", SourceBean.STATUS_WAIT)
            }, existSourceBean.id)
            LogA.e("资产${existSourceBean.pp_code}更新：$b (状态${existSourceBean.pp_act})")
        }else{
            LitePal.delete(SourceBean::class.java, existSourceBean.id)
        }
    }

    fun deleteSourcesByTask(taskId: String?, sources: List<SourceBean>?): Flowable<Boolean>{
        return Flowable.unsafeCreate<Boolean> {
            sources?.forEach {
                syncDeleteSourceByTask(taskId, it)
            }
            it.onNext(true)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun update(taskId: String?, source: SourceBean?): Flowable<SourceBean?>{
        return Flowable.unsafeCreate<SourceBean?> {
            val existSourceBean = LitePal.where("task_id = ? AND pp_code = ?", taskId, source?.pp_code).findFirst(SourceBean::class.java)
            if(existSourceBean != null){
                existSourceBean.apply {
                    pp_act = source?.pp_act?: pp_act
                    memo = source?.memo?: memo
                }

                val b = LitePal.update(SourceBean::class.java, ContentValues().apply {
                    put("pp_act", existSourceBean.pp_act)
                    put("memo", existSourceBean.memo)
                }, existSourceBean.id)

                LogA.e("资产${existSourceBean.pp_code}更新：$b (状态${existSourceBean.pp_act})")
            }

            it.onNext(existSourceBean)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun queryStatusQuantity(taskId: String?, where: String? = null): Flowable<Quantity>{
        return Flowable.unsafeCreate<Quantity>{
            it.onNext(Quantity(
                    LitePal.where("task_id = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}", taskId).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}", taskId, SourceBean.STATUS_WAIT).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}", taskId, SourceBean.STATUS_FINISH).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}", taskId, SourceBean.STATUS_PY).count(SourceBean::class.java),
                    LitePal.where("task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}", taskId, SourceBean.STATUS_PK).count(SourceBean::class.java)
            ))
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByPY(taskId: String?, pageNum: Int, where: String?): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val sql = "task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"} LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}"
            LogA.i(sql.replace("task_id = ?", "task_id = $taskId").replace("pp_act = ?", "pp_act = ${SourceBean.STATUS_PY}"))
            val data = LitePal.where(sql, taskId, SourceBean.STATUS_PY).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByFinish(taskId: String?, pageNum: Int, where: String?): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val sql = "task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"} LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}"
            LogA.i(sql.replace("task_id = ?", "task_id = $taskId").replace("pp_act = ?", "pp_act = ${SourceBean.STATUS_FINISH}"))
            val data = LitePal.where(sql, taskId, SourceBean.STATUS_FINISH).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByPK(taskId: String?, pageNum: Int, where: String?): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val sql = "task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"} LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}"
            LogA.i(sql.replace("task_id = ?", "task_id = $taskId").replace("pp_act = ?", "pp_act = ${SourceBean.STATUS_PK}"))
            val data = LitePal.where(sql, taskId, SourceBean.STATUS_PK).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByWait(taskId: String?, pageNum: Int, where: String? = null): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val sql = "task_id = ? AND pp_act = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"} LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}"
            LogA.i(sql.replace("task_id = ?", "task_id = $taskId").replace("pp_act = ?", "pp_act = ${SourceBean.STATUS_WAIT}"))
            val data = LitePal.where(sql, taskId, SourceBean.STATUS_WAIT).find(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceAll(taskId: String?, pageNum: Int = 0, where: String? = null): Flowable<MutableList<SourceBean>> {
        return Flowable.unsafeCreate<MutableList<SourceBean>> {
            val data =
                if(pageNum <= 0){
                    val sql = "task_id = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"}"
                    LogA.i(sql.replace("?", taskId?: "?"))
                    LitePal.where(sql, taskId).find(SourceBean::class.java)
                }else{
                    val sql = "task_id = ? ${if(TextUtils.isEmpty(where)) "" else "AND $where"} LIMIT $pageSize OFFSET ${pageSize * (pageNum - 1)}"
                    LogA.i(sql.replace("?", taskId?: "?"))
                    LitePal.where(sql, taskId).find(SourceBean::class.java)
                }
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

    fun querySourceByCode(taskId: String?, code: String?): Flowable<SourceBean?>{
        return Flowable.unsafeCreate<SourceBean?> {
            val data: SourceBean? = LitePal.where("task_id = ? AND pp_code = ?", taskId, code).findFirst(SourceBean::class.java)
            it.onNext(data)
            it.onComplete()
        }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
    }

}