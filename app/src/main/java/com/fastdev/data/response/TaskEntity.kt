package com.fastdev.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/20
 */
@Parcelize
class TaskEntity (
    var task_id: String = "",
    var task_name: String? = null,
    var task_info: String? = null, //描述
    var task_time: String? = null, //任务时间
    var task_complete_time: String? = null, //完成时间
    var task_status: String? = null, //10 待开始 20进行中 30完成  -1作废
    var task_pd_count: String? = "0", //总量
    var task_py_count: String? = "0", //盘盈数量
    var task_pk_count: String? = "0", //盘亏数量
    var task_wait_count: String? = "0", //待盘数量
    var task_complete_count: String? = "0" //已经盘点数量
): Parcelable{
    fun getStatus(): String {
        return when (task_status) {
            "10" -> "待开始"
            "20" -> "进行中"
            "30" -> "完成"
            "-1" -> "作废"
            else -> ""
        }
    }
}