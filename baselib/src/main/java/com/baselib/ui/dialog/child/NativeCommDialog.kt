package com.baselib.ui.dialog.child

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.baselib.R
import com.baselib.helper.DialogHelper
import com.baselib.ui.dialog.NativeBaseDialog
import com.baselib.ui.dialog.callback.MenuDialogCallback

/**
 * @作者： tangdx
 * @创建时间： 2018\4\17 0017
 * @功能描述： 提示框
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
class NativeCommDialog(context: Context) : NativeBaseDialog(context, R.layout.baselib_dialog_commtips_view) {
    private var autoDismiss = true

    lateinit var tvTitle: TextView
    lateinit var tvContent: TextView
    lateinit var tvBtnLeft: TextView
    lateinit var tvBtnCenter: TextView
    lateinit var tvBtnRight: TextView

    /**
     * 设置是否点击按钮会自动关闭弹框
     */
    fun setAutoDismiss(autoDismiss: Boolean): NativeCommDialog {
        this.autoDismiss = autoDismiss
        return this
    }

    fun setTitle(title: String?): NativeCommDialog{
        if(TextUtils.isEmpty(title)){
            tvTitle.visibility = View.GONE
        }else{
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
        return this
    }

    fun setContent(content: String?): NativeCommDialog{
        if(TextUtils.isEmpty(content)){
            tvContent.visibility = View.GONE
        }else{
            tvContent.visibility = View.VISIBLE
            tvContent.text = content
        }
        return this
    }

    fun setLeftMenu(callbackLeft: MenuDialogCallback?): NativeCommDialog{
        if (callbackLeft != null) {
            tvBtnLeft.visibility = View.VISIBLE
            tvBtnLeft.text = callbackLeft?.menuText
            tvBtnLeft.setOnClickListener {
                callbackLeft?.onClick(super.rootView, super.dialog)
                if (autoDismiss) hide()
            }
        } else {
            tvBtnLeft.visibility = View.GONE
        }
        changeListener()
        return this
    }

    fun setCenterMenu(callbackCenter: MenuDialogCallback?): NativeCommDialog {
        if (callbackCenter != null) {
            tvBtnCenter.visibility = View.VISIBLE
            tvBtnCenter.text = callbackCenter?.menuText
            tvBtnCenter.setOnClickListener {
                callbackCenter?.onClick(super.rootView, dialog)
                if (autoDismiss) hide()
            }
        } else {
            tvBtnCenter.visibility = View.GONE
        }
        changeListener()
        return this
    }

    fun setRightMenu(callbackRight: MenuDialogCallback?): NativeCommDialog {
        if (callbackRight != null) {
            tvBtnRight.visibility = View.VISIBLE
            tvBtnRight.text = callbackRight?.menuText
            tvBtnRight.setOnClickListener {
                callbackRight?.onClick(super.rootView, dialog)
                if (autoDismiss) hide()
            }
        } else {
            tvBtnRight.visibility = View.GONE
        }
        changeListener()
        return this
    }

    private fun changeListener() {
        if (tvBtnLeft.visibility == View.GONE &&
                tvBtnCenter.visibility == View.GONE &&
                tvBtnRight.visibility == View.GONE) {
            super.rootView.findViewById<View>(R.id.view_line).visibility = View.GONE
            super.rootView.findViewById<View>(R.id.layout_menu).visibility = View.GONE
        } else {
            super.rootView.findViewById<View>(R.id.view_line).visibility = View.VISIBLE
            super.rootView.findViewById<View>(R.id.layout_menu).visibility = View.VISIBLE
        }
    }

    override fun initView(dialog: Dialog, dialogRootView: View) {
        tvTitle = dialogRootView.findViewById(R.id.tv_common_prompt_title)
        tvContent = dialogRootView.findViewById(R.id.tv_common_prompt_content)
        tvBtnLeft = dialogRootView.findViewById(R.id.btn_common_prompt_left)
        tvBtnCenter = dialogRootView.findViewById(R.id.btn_common_prompt_center)
        tvBtnRight = dialogRootView.findViewById(R.id.btn_common_prompt_right)
    }

}