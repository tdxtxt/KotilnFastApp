package com.fastdev.data.response

import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/11
 */
@Parcelize
data class SourceBean(
    val id: Long = 0,

    var task_id: String = "", //所属任务id

    var pp_code: String = "", //资产编码

    var pp_name: String? = null, //资产名称

    var pp_type: String? = null, //资产类型

    var pp_model: String? = null, //规格型号

    var pp_addr: String? = null, //存放详细地址

    var pp_status: String? = null, //资产状态 10 正常  20闲置  30异常  -1 报废

    @SerializedName("act")
    var pp_act: String = STATUS_WAIT, //盘点状态 0 待盘   10 盘盈  20盘亏  30 已盘

    var building_code: String? = null, //楼栋id

    var floor_code: String? = null, //楼层id

    var house_code: String? = null, //房间id

    var memo: String? = null //备注
): LitePalSupport(), Parcelable {
    companion object{
        const val STATUS_ALL = "all"
        const val STATUS_WAIT = "0" //待盘
        const val STATUS_PY = "10" //盘盈
        const val STATUS_PK = "20" //盘亏
        const val STATUS_FINISH = "30" //已盘
    }
    fun getStatusName(): String{
        return when (pp_act) {
            STATUS_FINISH -> "已盘"
            STATUS_PY -> "盘盈"
            STATUS_PK -> "盘亏"
            STATUS_WAIT -> "待盘"
            else -> pp_act
        }
    }

    fun getNonullName() = if(TextUtils.isEmpty(pp_name)) "未知" else pp_name

    fun getNonullType() = if(TextUtils.isEmpty(pp_type)) "未知" else pp_type

    fun getNonullModel() = if(TextUtils.isEmpty(pp_model)) "未知" else pp_model

    fun getNonullAddr() = if(TextUtils.isEmpty(pp_addr)) "未知" else pp_addr

    override fun hashCode(): Int {
        return "sourceBean_${task_id}_${pp_code}".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other?.hashCode()
    }
}