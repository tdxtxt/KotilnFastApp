package com.fastdev.data.db

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/11
 */
class Source : LitePalSupport() {
    private val id: Int = 0

    @Column
    var taskId: String? = null

    var name: String? = null

    var uuid: String? = null

    var dongFrom: String? = null

    var foorFrom: String? = null

    var roomFrom: String? = null

    var dong: String? = null

    var foor: String? = null

    var room: String? = null

    var status: Int = 0
}