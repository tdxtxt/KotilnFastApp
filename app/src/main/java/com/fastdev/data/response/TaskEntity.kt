package com.fastdev.data.response

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/20
 */
class TaskEntity {
    var task_id: String? = null
    var task_name: String? = null
    var task_info: String? = null //描述
    var task_pd_count: String? = null //总量
    var task_time: String? = null //任务时间
    var task_complete_time: String? = null //完成时间
    var task_status: String? = null //10 待开始 20进行中 30完成  -1作废
    var task_py_count: Int = 0 //盘盈数量
    var task_pk_count: Int = 0 //盘亏数量
    var task_wait_count: Int = 0 //待盘数量
    var task_complete_count: Int = 0 //已经盘点数量
}