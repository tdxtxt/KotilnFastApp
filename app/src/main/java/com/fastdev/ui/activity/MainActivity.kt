package com.fastdev.ui.activity

import com.baselib.helper.ImageLoaderHelper
import com.baselib.helper.LogA
import com.baselib.helper.composeBindLifecycle
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.net.ApiClient
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.activity_main.iv_test

class MainActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_main

    override fun initUi() {
        showContentView()
        ImageLoaderHelper.loadImage(iv_test, "https://www.dogedoge.com/rmt/RIPhuccrX6mfmtQTwR19gCTRzrmzizkZxoNTvYUBvA-h4T7mIhTpoxRKO7?w=212&h=130")
        iv_test.setOnClickListener {  ApiClient.getService().testApi1().composeBindLifecycle(this).subscribe({ LogA.i(it) }, { LogA.i(it) }) }
    }


}
