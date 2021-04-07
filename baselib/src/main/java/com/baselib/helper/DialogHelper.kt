package com.baselib.helper

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.baselib.callback.MenuDialogCallback
import com.baselib.ui.dialog.child.CommDialog
import com.baselib.ui.dialog.child.ProgressDialog
import com.lxj.xpopup.core.BasePopupView

/**
 * dialog工具类
 */
object DialogHelper {
    fun showCommDialog(context: FragmentActivity?, title: String?, content: String?,
                       caneleable: Boolean = true,
                       isAutoDismiss: Boolean = true,
                       leftMenu: (MenuDialogCallback.() -> Unit)? = null,
                       rightMenu: (MenuDialogCallback.() -> Unit)? = null): CommDialog? {
        if (context == null) return null
        return CommDialog(context = context).setTitle(title)
                .setAutoDismiss(isAutoDismiss)
                .setContent(content)
                .setLeftMenu(leftMenu)
                .setCenterMenu(null)
                .setRightMenu(rightMenu).apply { setCancelable(caneleable).show() }
    }

    fun createProgressDialog(activity: FragmentActivity, desc: String, cancelable: Boolean): ProgressDialog {
        return ProgressDialog(activity).apply { setDesc(desc)?.setCancelable(cancelable) }
    }

    fun showTips(context: FragmentActivity, content: String?){
        showCommDialog(context, "温馨提示", content)
    }
}

fun Dialog.lifecycleOwner(owner: LifecycleOwner? = null): Dialog {
    val observer = DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
            ?: throw IllegalStateException(
                    "$context is not a LifecycleOwner."
            ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

fun BasePopupView.lifecycleOwner(owner: LifecycleOwner? = null): BasePopupView {
    val observer = DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
            ?: throw IllegalStateException(
                    "$context is not a LifecycleOwner."
            ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

internal class DialogLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss()
//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    fun onPause() = dismiss()
}