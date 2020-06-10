package com.fastdev.ui.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.baselib.helper.ScreenHelper
import com.baselib.ui.dialog.NativeBaseDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/6/4
 */
class ShareDialog(context : Context) : NativeBaseDialog(context, R.layout.dialog_share) {
    var recyclerview: RecyclerView? = null
    override fun initView(dialog: Dialog, rootView: View) {
        recyclerview = rootView.findViewById(R.id.recyclerview)
        recyclerview?.layoutManager = GridLayoutManager(context, 4)
        recyclerview?.adapter = object: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_share, mutableListOf("","", "")){
            override fun convert(helper: BaseViewHolder, item: String?) {
                helper.setText(R.id.tv_name, "微信").setImageResource(R.id.iv_icon, R.mipmap.ic_launcher)
            }
        }
    }

    override fun provideDialogWidth(context: Context): Int {
        return ScreenHelper.getScreenWidth(context)
    }
}