package com.baselib.ui.fragment.impl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baselib.callback.StartForResultListener

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/29
 */
class StartForResultFragment1 : Fragment() {
    private var listener: StartForResultListener? = null
    fun setListener(callback: (StartForResultListener.() -> Unit)?){
        if(callback != null){
            this.listener = object : StartForResultListener() {}
            this.listener?.callback()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            listener?.activityResult?.invoke(requestCode, resultCode, data)
        }else{
            listener?.cancel?.invoke()
        }
    }

}