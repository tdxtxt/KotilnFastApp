package com.fastdev.core

import com.baselib.rx.event.RxBus
import com.fastdev.data.event.TaskEventCode
import com.seuic.scankey.IKeyEventCallback

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/31
 */
class SKeyEventCallback: IKeyEventCallback.Stub() {

    override fun onKeyDown(keyCode: Int) {
        if(250 == keyCode){
            RxBus.send(TaskEventCode.KEY_DOWN)
        }
    }

    override fun onKeyUp(keyCode: Int) {
        if(250 == keyCode){
            RxBus.send(TaskEventCode.KEY_UP)
        }
    }
}