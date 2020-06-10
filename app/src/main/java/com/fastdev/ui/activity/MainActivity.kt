package com.fastdev.ui.activity

import com.baselib.helper.ImageLoaderHelper
import com.baselib.helper.LogA
import com.baselib.helper.composeBindLifecycle
import com.baselib.helper.composeProgress
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.helper.SocialHelper
import com.fastdev.net.ApiClient
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.activity_main.iv_test

class MainActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_main

    override fun initUi() {
        showContentView()
        ImageLoaderHelper.loadImage(iv_test, "https://mtest.sanxiapay.com/uxunimg/cust/shop/syscontent/2018/01/8/98618207493227154252.bmp")
        iv_test.setOnClickListener {
//            SocialHelper.shareText(activity, "", "")
            ApiClient.getService().testApi2("sss").composeBindLifecycle(this).composeProgress(getProgressBar()).subscribe({ LogA.i(it) }, { LogA.i(it) })
        }
    }


}
