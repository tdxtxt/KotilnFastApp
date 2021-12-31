package com.fastdev.ui.dialog

import android.view.View
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.baselib.helper.LogA
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.core.UHFSdk
import com.fastdev.ui.R
import io.reactivex.Flowable

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/18
 */
class ConfigDialog constructor(activity: FragmentActivity) : BottomBaseDialog(activity) {
    private var spinner: Spinner? = null
    private var listPower = listOf(10, 20, 30)
    override fun getLayoutId() = R.layout.dialog_config

    override fun onCreate(dialog: IBDialog) {
        spinner = findViewById(R.id.spinner)
        val defaultValue = UHFSdk.getPower()
        LogA.i("power = $defaultValue")
        if(defaultValue <= 10){
            spinner?.setSelection(0)
            if(defaultValue != 10) UHFSdk.setPower(10)
        }else if(defaultValue <= 20){
            spinner?.setSelection(1)
            if(defaultValue != 20) UHFSdk.setPower(20)
        }else{
            spinner?.setSelection(2)
            if(defaultValue != 30) UHFSdk.setPower(30)
        }

        findViewById<View>(R.id.iv_close)?.setOnClickListener {
            dismiss()
        }
        findViewById<View>(R.id.btn_next)?.setOnClickListener {
            UHFSdk.setPower(listPower[spinner?.selectedItemPosition?: 0])
            dismiss()
        }
    }

}