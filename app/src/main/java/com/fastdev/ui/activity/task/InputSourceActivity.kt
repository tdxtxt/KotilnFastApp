package com.fastdev.ui.activity.task

import android.app.Activity
import com.baselib.callback.StartForResultListener
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/22
 */
class InputSourceActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_source_input

    override fun initUi() {
        setTitleBar("资产录入")
    }

    companion object{
        fun open(activity: Activity?, action: (SourceBean?) -> Unit){
            activity?.startActivityForResult(InputSourceActivity::class.java, null) {
                onActivityResult { requestCode, resultCode, data ->
                    action.invoke(null)
                }
            }
        }
    }
}