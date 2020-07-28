package com.baselib.ui.dialog.callback

import android.view.View
import com.baselib.ui.dialog.impl.IBDialog

abstract class MenuDialogCallback{
    var menuText = ""
    var click: ((rootView: View?, dialog: IBDialog?) -> Unit)? = null

    fun onClick(click: (rootView: View?, dialog: IBDialog?) -> Unit){
        this.click = click
    }
}