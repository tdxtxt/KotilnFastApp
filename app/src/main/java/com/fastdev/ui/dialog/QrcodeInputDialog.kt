package com.fastdev.ui.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class QrcodeInputDialog constructor(activity: FragmentActivity) : CenterBaseDialog(activity) {
    override fun getLayoutId() = R.layout.dialog_input_qrcode

    override fun onCreate(dialog: IBDialog) {

        findViewById<View>(R.id.btn_cancel)?.setOnClickListener {
            dismiss()
        }

        findViewById<View>(R.id.btn_query)?.setOnClickListener {

        }
    }
}