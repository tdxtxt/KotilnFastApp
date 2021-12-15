package com.baselib.helper

import android.app.Dialog
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.baselib.callback.MenuCallBack
import com.baselib.ui.dialog.child.CommDialog
import com.baselib.ui.dialog.child.ProgressDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.impl.FullScreenPopupView

/**
 * dialog工具类
 */
object DialogHelper {
    fun showCommDialog(activity: FragmentActivity?, title: String?, content: String?,
                       leftMenu: (MenuCallBack.() -> Unit)? = null,
                       rightMenu: (MenuCallBack.() -> Unit)? = null): CommDialog? {
        if (activity == null) return null
        return createCommDialog(activity, title, content, leftMenu, rightMenu)?.apply { show() }
    }

    fun createCommDialog(activity: FragmentActivity?, title: String?, content: String?,
                         leftMenu: (MenuCallBack.() -> Unit)? = null,
                         rightMenu: (MenuCallBack.() -> Unit)? = null): CommDialog? {
        if (activity == null) return null
        return CommDialog(activity).setTitle(title)
                .setContent(content)
                .setLeftMenu(leftMenu)
                .setCenterMenu(null)
                .setRightMenu(rightMenu)
    }

    fun createProgressDialog(activity: FragmentActivity, desc: String = "正在加载...", cancelable: Boolean): ProgressDialog {
        return ProgressDialog(activity).apply { setDesc(desc)?.setCancelable(cancelable) }
    }

    fun showTips(context: FragmentActivity, content: String?){
        showCommDialog(context, "温馨提示", content, leftMenu = {

        })
    }

    /**
     * atView 依附的view
     * popupPosition 相对atView的哪个位置
     * shapePercent  三角架的百分比位置【0-1】
     * offsetX 微调x轴的位置
     * offsetY 微调y轴的位置
     * tabTexts 显示的文字内容
     * tabIcons 显示的图标
     * callback 选择后的回调
     */
    fun showPopupAtView(activity: FragmentActivity, atView: View, popupPosition: PopupPosition, shapePercent: Float = 0.5f, offsetX: Int = 0, offsetY: Int = 0, tabTexts: List<String>, tabIcons: List<Int>? = null, callback: ((position: Int, text: String) -> Unit)? = null){
        XPopup.Builder(activity)
                .hasShadowBg(false).isDarkTheme(true).popupAnimation(null)
                .offsetX(offsetX).offsetY(offsetY)
                .atView(atView).popupPosition(popupPosition, shapePercent).asAttachList(
                        tabTexts.toTypedArray(), tabIcons?.toIntArray()){ position, text ->
                    callback?.invoke(position, text)
                }.show()
    }

    fun showFullScreenDialog(activity: FragmentActivity?, cancelable: Boolean, popupView: FullScreenPopupView){
        if(activity == null) return
        XPopup.Builder(activity)
                .hasStatusBarShadow(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .dismissOnBackPressed(cancelable)
                .asCustom(popupView)
                .show()
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