package com.fastdev.ui.dialog

import androidx.fragment.app.FragmentActivity
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/18
 */
class ConfigDialog constructor(activity: FragmentActivity) : BottomBaseDialog(activity) {
    override fun getLayoutId() = R.layout.dialog_config

    override fun onCreate(dialog: IBDialog) {
    }
}