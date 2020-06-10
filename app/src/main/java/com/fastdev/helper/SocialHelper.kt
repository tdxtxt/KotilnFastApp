package com.fastdev.helper

import android.app.Activity
import com.baselib.ui.dialog.NativeBaseDialog
import com.fastdev.ui.dialog.ShareDialog

/**
 * 功能描述:三方工具
 * @author tangdexiang
 * @since 2020/6/2
 */
object SocialHelper {
    //分享文本
    fun shareText(activity: Activity, title: String, content: String){
        ShareDialog(activity).showAtBottom<NativeBaseDialog>()
    }
    //分享图片
    fun shareImage(imageUrl: String, desc: String){

    }
    //分享链接
    fun shareHerf(title: String, content: String, targetUrl: String, thumbImagePath: String){

    }

}