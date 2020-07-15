package com.fastdev.ui.activity.test

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.baselib.helper.ImageLoaderHelper
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_main

    override fun initUi() {
        showContentView()
        ImageLoaderHelper.loadImage(iv_test, "https://mtest.sanxiapay.com/uxunimg/cust/shop/syscontent/2018/01/8/98618207493227154252.bmp")
        iv_test.setOnClickListener {
//            SocialHelper.shareText(activity, "", "")
//            ApiClient.getService().testApi2("sss").composeBindLifecycle(this).composeProgress(getProgressBar()).subscribe({ LogA.i(it) }, { LogA.i(it) })
            startOtherApp("com.hundsun.cqsxbank.mobilepay")
        }
    }


    private fun startOtherApp(packageName: String){
        startActivity(packageManager.getLaunchIntentForPackage(packageName).apply { setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    fun isInstallApp(context: Context, packageName: String): Boolean{
        return if (TextUtils.isEmpty(packageName)) false else try {
            val info: ApplicationInfo = context.packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
