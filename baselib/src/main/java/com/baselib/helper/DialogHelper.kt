package com.baselib.helper

import android.app.Activity
import android.content.Context
import com.baselib.ui.dialog.callback.MenuDialogCallback
import com.baselib.ui.dialog.child.NativeCommDialog
import com.baselib.ui.dialog.child.NativeProgressDialog

/**
 * dialog工具类
 */
class DialogHelper {
    fun showCommDialog(context: Context?, title: String?, content: String?, caneleable: Boolean = true,
                       isAutoDismiss: Boolean = true,
                       leftMenu: MenuDialogCallback? = null,rightMenu: MenuDialogCallback? = null): NativeCommDialog?{
        if(context == null) return null
        val commDialog = NativeCommDialog(context).setTitle(title)
                .setContent(content)
                .setLeftMenu(leftMenu)
                .setCenterMenu(null)
                .setRightMenu(rightMenu)
        commDialog.show<NativeCommDialog>()
        return commDialog
    }

    fun createProgressDialog(activity: Activity, desc: String, cancelable: Boolean): NativeProgressDialog {
        return NativeProgressDialog(activity).setDesc(desc).setCancelable(cancelable)
    }

    companion object {
        private val instance: DialogHelper by lazy { DialogHelper() }
    }
}