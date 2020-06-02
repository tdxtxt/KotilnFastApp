package com.baselib.ui.dialog.callback

import android.app.Dialog
import android.support.annotation.StringRes
import android.view.View
import com.baselib.app.DevApp

open class MenuDialogCallback constructor(val menuText: String?){
    constructor(): this("") {}

    constructor(@StringRes menuTextResId: Int): this(DevApp.mContext?.getString(menuTextResId)) {}

    fun onClick(rootView: View, dialog: Dialog) {}
}