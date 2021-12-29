package com.fastdev.data.event

import com.baselib.rx.event.RxEventCode

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/13
 */
abstract class TaskEventCode(code: Int) : RxEventCode(code) {

    companion object{
        val COMMIT_SUCCESS = object : TaskEventCode(10111) {}
    }

}