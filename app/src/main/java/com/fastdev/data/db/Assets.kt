package com.fastdev.data.db

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/11
 */
class Assets : LitePalSupport() {
    @Column(nullable = false)
    val name: String? = null

    @Column(nullable = false)
    val uuid: String? = null

    @Column(nullable = false)
    val oneLevelType: String? = null

    @Column(nullable = false)
    val twoLevelType: String? = null
}