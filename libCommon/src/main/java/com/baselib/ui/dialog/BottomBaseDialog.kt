package com.baselib.ui.dialog

import android.app.Activity
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.Window
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import com.baselib.helper.lifecycleOwner
import com.baselib.ui.dialog.impl.IBDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.XPopupCallback

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/28
 */
abstract class BottomBaseDialog(val context: FragmentActivity) : IBDialog {
    val dialog: BottomPopupView = object : BottomPopupView(context){
        override fun getImplLayoutId() = getLayoutId()
        override fun getMaxWidth(): Int {
            return super.getMaxWidth()
        }
        override fun getMaxHeight(): Int {
            return super.getMaxHeight()
        }
        override fun getPopupWidth(): Int {
            return getDialogWidth()
        }
        override fun getPopupHeight(): Int {
            return getDialogHeight()
        }
    }

    val builder: XPopup.Builder = XPopup.Builder(context).setPopupCallback(object : XPopupCallback {
        override fun onBackPressed(popupView: BasePopupView?) = false  //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
        override fun configWindow(window: Window?) {
        }
        override fun onDismiss(popupView: BasePopupView?) {
            mCancelListener?.invoke()
        }
        override fun beforeShow(popupView: BasePopupView?) {
        }
        override fun onCreated(popupView: BasePopupView?) {
            this@BottomBaseDialog.apply { onCreate(this) }
        }
        override fun beforeDismiss(popupView: BasePopupView?) {
        }
        override fun onShow(popupView: BasePopupView?) {
        }
    })
    var popupView: BasePopupView? = null

    override fun show(): BottomBaseDialog {
        popupView?.dismiss()
        return builder.asCustom(dialog).apply { popupView = this }.show().lifecycleOwner(context).run { this@BottomBaseDialog }
    }

    override fun dismiss() {
        popupView?.dismiss()
    }

    override fun hide() {
        popupView?.dismiss()
    }

    override fun getActivity(): Activity {
        if (context != null && context is Activity) return context
        var bindAct: Activity? = null
        var context = dialog.context
        do {
            if (context is Activity) {
                bindAct = context
                break
            } else if (context is ContextThemeWrapper) {
                context = context.baseContext
            } else {
                break
            }
        } while (true)
        return bindAct!!
    }

    override fun getRootView() = dialog

    var mCancelListener: (() -> Unit)? = null
    override fun setCancelListener(cancelListener: () -> Unit): BottomBaseDialog {
        mCancelListener = cancelListener
        return this
    }

    var mCancelable = true
    override fun setCancelable(cancelable: Boolean): BottomBaseDialog {
        mCancelable = cancelable
        builder.dismissOnTouchOutside(if(mCancelable) mCancelableOnTouchOutside else false)
        builder.dismissOnBackPressed(mCancelable)
        return this
    }

    var mCancelableOnTouchOutside = true
    override fun setCancelableOnTouchOutside(cancelableOnTouchOutside: Boolean): BottomBaseDialog {
        mCancelableOnTouchOutside = cancelableOnTouchOutside
        builder.dismissOnTouchOutside(if(mCancelable) cancelableOnTouchOutside else false)
        return this
    }

    override fun <T : View> findViewById(@IdRes id: Int): T? {
        return dialog.findViewById(id)
    }

    override fun getMaxWidth() = 0

    override fun getMaxHeight() = 0

    override fun getDialogWidth() = 0

    override fun getDialogHeight() = 0
}