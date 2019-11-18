package com.baselib.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.*
import com.baselib.R
import com.baselib.helper.ScreenHelper

/**
 * @作者： tangdx
 * @创建时间： 2018\4\17 0017
 * @功能描述： dialog基类;没有采用三方框架
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
open class NativeBaseDialog constructor(val context: Context, layoutId: Int, style: Int){
    protected var dialog: Dialog = AppCompatDialog(context,style)
            .apply {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
    protected lateinit var rootView: View
    //是否消失
    private var mCancelable = true
    //是否点击弹框外消失
    private var mCancelableOnTouchOutside = true

    constructor(context: Context, layoutId: Int): this(context,layoutId, R.style.smart_show_dialog)

    init {

        inflate(context, layoutId, null)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(OnViewGlobalLayoutListener(rootView, provideDialogMaxHeight(context)))
        initView(dialog,rootView)

        val rootLp = ViewGroup.MarginLayoutParams(provideDialogWidth(context), provideDialogHeight(context))
        setCancelableOnTouchOutside<NativeBaseDialog>(true)

        dialog.apply {
            setContentView(rootView,rootLp)
            setOnShowListener {  }
            setOnDismissListener { onDestroy() }
        }
    }

    /**
     * 设置是否可取消弹框【点击弹框外部窗口、返回按键的屏蔽】
     */
    fun <T : NativeBaseDialog> setCancelable(cancelable: Boolean): T {
        this.mCancelable = cancelable
        dialog.setCancelable(mCancelable)
        return this as T
    }

    /**
     * 设置点击弹框外部窗口是否可取消弹框
     */
    fun <T : NativeBaseDialog> setCancelableOnTouchOutside(cancelableOnTouchOutside: Boolean): T {
        this.mCancelableOnTouchOutside = cancelableOnTouchOutside
        dialog.setCanceledOnTouchOutside(if (mCancelable) mCancelableOnTouchOutside else false)
        return this as T
    }

    /**
     * 子类都必须调用此方法
     */
    fun <T : NativeBaseDialog> show(): T {
        if (dialog == null) return this as T
        if (getActivity() != null && getActivity()!!.isFinishing()) return this as T
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
        return this as T
    }

    /**
     * 隐藏dialog
     */
    fun hide() {
        if (dialog == null || !dialog.isShowing) return

        val bindAct = getActivity()
        if (bindAct != null && !bindAct!!.isFinishing())
            dialog.dismiss()
    }

    /**
     * 子类若要手动释放资源，需重写此方法
     */
    fun onDestroy() {
        hide()
    }

    private fun getActivity(): Activity? {
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
        return bindAct
    }

    open fun initView(dialog: Dialog, rootView: View) {

    }

    private fun inflate(context: Context, layoutId: Int, root: ViewGroup?) {
        rootView = View.inflate(context, layoutId, root)
    }

    /**
     * 弹框的宽度
     */
    open fun provideDialogWidth(context: Context): Int {
        return Math.min(ScreenHelper.getScreenWidth(context) - ScreenHelper.dp2px(context, 70f), ScreenHelper.dp2px(context, 290f))
    }
    /**
     * 弹框的高度
     */
    open fun provideDialogHeight(context: Context): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * 允许弹框的最大高度
     */
    open fun provideDialogMaxHeight(context: Context): Int {
        return ScreenHelper.getScreenHeight(context) * 2 / 3
    }

    inner class OnViewGlobalLayoutListener constructor(val view : View, val maxHeight : Int = 500) : ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            if (view.getHeight() > maxHeight)
                view.getLayoutParams().height = maxHeight
        }
    }
}