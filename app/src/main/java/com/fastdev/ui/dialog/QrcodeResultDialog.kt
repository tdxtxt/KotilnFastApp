package com.fastdev.ui.dialog

import androidx.fragment.app.FragmentActivity
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class QrcodeResultDialog constructor(activity: FragmentActivity) : CenterBaseDialog(activity) {
    override fun getLayoutId() = R.layout.dialog_qrcode_result

    override fun onCreate(dialog: IBDialog) {
    }
}