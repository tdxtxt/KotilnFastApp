package com.baselib.ui.dialog.child

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.baselib.R
import com.baselib.ui.dialog.NativeBaseDialog
/**
 * @作者： tangdx
 * @创建时间： 2018\4\17 0017
 * @功能描述： 菊花转
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
class NativeProgressDialog(context : Context) : NativeBaseDialog(context, R.layout.baselib_dialog_commprogress_view) {
    private lateinit var tvDesc: TextView

    override fun initView(dialog: Dialog, rootView: View) {
        tvDesc = rootView.findViewById(R.id.tv_desc)
        setDesc("")
    }

    fun setDesc(desc: String?) = tvDesc.run { text = desc?: ""; this@NativeProgressDialog }

    override fun provideDialogWidth(context: Context) = ViewGroup.LayoutParams.MATCH_PARENT

    override fun provideDialogHeight(context: Context) = ViewGroup.LayoutParams.MATCH_PARENT
}
