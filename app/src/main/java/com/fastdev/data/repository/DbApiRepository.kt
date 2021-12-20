package com.fastdev.data.repository

import com.fastdev.data.db.Source
import com.fastdev.data.repository.base.BaseRepository
import org.litepal.LitePal

/**
 * 功能描述: 数据库操作仓库
 * @author tangdexiang
 * @since 2021/12/20
 */
class DbApiRepository : BaseRepository() {
    fun saveSource(bean: Source){
        bean.save()
    }

    fun saveSources(beans: List<Source>){
        LitePal.saveAll(beans)
    }

    fun querySource() = LitePal.findFirst(Source::class.java)
}