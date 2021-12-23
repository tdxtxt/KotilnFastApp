package com.fastdev.data.response

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/11
 */
class SourceBean : LitePalSupport() {
    companion object{
        val STATUS_PY = "10" //盘盈
        val STATUS_PK = "20" //盘亏
        val STATUS_WAIT = "0" //待盘
        val STATUS_FINISH = "1" //已盘
    }
    private val id: Int = 0

    var task_id: String = "" //所属任务id

    var pp_code: String = "" //资产编码

    var pp_name: String? = null //资产名称

    var pp_model: String? = null //规格型号

    var pp_addr: String? = null //存放详细地址

    var pp_status: String? = null //资产状态 10 正常  20闲置  30异常  -1 报废

    var pp_act: String = STATUS_WAIT //盘点状态 0 待盘  1 已盘 10 盘盈  20盘亏

    var building_code: String? = null //楼栋id

    var floor_code: String? = null //楼层id

    var house_code: String? = null //房间id

    var memo: String? = null //备注
}