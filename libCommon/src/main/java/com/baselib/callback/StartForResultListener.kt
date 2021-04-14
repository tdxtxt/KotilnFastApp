package com.baselib.callback

import android.content.Intent


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/29
 */
abstract class StartForResultListener {
    var activityResult: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null
    var cancel: (() -> Unit)? = null

    fun onActivityResult(callback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)?){
        this.activityResult = callback
    }

    fun onCancel(callback: (() -> Unit)?){
        this.cancel = callback
    }
}