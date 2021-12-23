package com.fastdev.data.request

import com.fastdev.data.response.SourceBean

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/23
 */
class SourceRequest(
    var task_id: String? = null,
    var rows: List<SourceBean>? = null
)