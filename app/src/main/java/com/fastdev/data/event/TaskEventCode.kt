package com.fastdev.data.event

import com.baselib.rx.event.RxEventCode

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/13
 */
abstract class TaskEventCode(code: Int, data: Any? = null) : RxEventCode(code, data) {

    companion object{
        val DELETE_TASK = object : TaskEventCode(10111) {}
    }

}