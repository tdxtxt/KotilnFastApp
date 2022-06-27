package com.fastdev.data.response

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/21
 */
class SourceResp {
    var detail: TaskEntity? = null
    var property_list: MutableList<SourceBean>? = null
    var area_list: List<PlaceBean>? = null

    fun isEmpty() = property_list?.size?:0 == 0
}