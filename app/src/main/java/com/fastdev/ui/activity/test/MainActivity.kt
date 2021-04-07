package com.fastdev.ui.activity.test

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.baselib.helper.DialogHelper
import com.baselib.helper.ImageLoaderHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.bean.body.CardType
import com.fastdev.ui.R
import com.fastdev.ui.dialog.ShareDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.pingerx.socialgo.core.model.ShareEntity
import kotlinx.android.synthetic.main.activity_main.*
import org.jaaksi.pickerview.picker.OptionPicker


class MainActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_main

    override fun initUi() {
        showContentView()
        ImageLoaderHelper.loadImage(iv_test, "https://mtest.sanxiapay.com/uxunimg/cust/shop/syscontent/2018/01/8/98618207493227154252.bmp")
        iv_test.setOnClickListener {
//            SocialHelper.shareText(activity, "", "")
//            ApiClient.getService().testApi2("sss").composeBindLifecycle(this).composeProgress(getProgressBar()).subscribe({ LogA.i(it) }, { LogA.i(it) })
//            startOtherApp("com.hundsun.cqsxbank.mobilepay")
//            ShareDialog(activity = activity).show(ShareEntity.CREATOR.buildImageObj("https://camo.githubusercontent.com/f03e30826c75c61c21d287c537e4d4d707afc6d4/687474703a2f2f7777312e73696e61696d672e636e2f6c617267652f37316233386632636c793167316a6e616732647a626a3230366130383277656d2e6a7067"))
//            DialogHelper.showCommDialog(activity, "温馨提示", "你好我好大家好喜喜呃", leftMenu = {menuText = "确认"}, rightMenu = {menuText = "取消"})?.setCancelable(false)

//            XPopup.Builder(activity).asConfirm("你好", "xiexie"){

//            }.show()
            XPopup.Builder(activity)
                    .hasShadowBg(false).isDarkTheme(true).popupAnimation(null).offsetX(10)
                    .atView(it).popupPosition(PopupPosition.Bottom,0.9f).asAttachList(
                            arrayOf("账户管理", "首页"), null){ position, text ->
                    }.show()

            /*OptionPicker.Builder(this, 1, OptionPicker.OnOptionSelectListener { picker, selectedPosition, selectedOptions ->
            }).create().apply {
                setData(CardType.getCardTypes())
                show()
            }*/
        }
    }


    private fun startOtherApp(packageName: String){
        if(isInstallApp(this, packageName))
            startActivity(packageManager.getLaunchIntentForPackage(packageName).apply { setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    fun isInstallApp(context: Context, packageName: String): Boolean{
        return if (TextUtils.isEmpty(packageName)) false else try {
            val info: ApplicationInfo = context.packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            ToastHelper.showToast("未安装")
            false
        }
    }

    // app 是否安装
    fun isAppInstall(context: Context, pkgName: String): Boolean {
        val pm = context.packageManager ?: return false
        val packages = pm.getInstalledPackages(0)
        var result = false
        for (info in packages) {
            if (TextUtils.equals(info.packageName.toLowerCase(), pkgName)) {
                result = true
                break
            }
        }
        return result
    }
}
