package com.baselib.callback

import android.text.TextUtils
import android.view.View
import com.baselib.ui.dialog.impl.IBDialog

abstract class MenuCallBack{
    var icon : Int = 0
    var menuText: CharSequence = ""
    var click: ((rootView: View?, any: Any?) -> Unit)? = null

    fun onClick(click: (rootView: View?, any: Any?) -> Unit){
        this.click = click
    }


    open fun isTextMenu(): Boolean {
        return !TextUtils.isEmpty(menuText)
    }

    open fun isIconMenu(): Boolean {
        return this.icon > 0
    }
}