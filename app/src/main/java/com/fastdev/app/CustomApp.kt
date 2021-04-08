package com.fastdev.app

import android.app.Application
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import com.baselib.app.DevApp
import com.baselib.helper.ActStackHelper
import com.baselib.net.NetMgr
import com.fastdev.net.config.TonNetProvider
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import org.jaaksi.pickerview.dialog.DefaultPickerDialog
import org.jaaksi.pickerview.dialog.IGlobalDialogCreator
import org.jaaksi.pickerview.picker.BasePicker
import org.jaaksi.pickerview.widget.DefaultCenterDecoration
import org.jaaksi.pickerview.widget.PickerView

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 * https://guolin.blog.csdn.net/article/details/109787732
 */
@HiltAndroidApp
class CustomApp : DevApp() {
    override fun isLoggable() = true

    override fun onCreate() {
        super.onCreate()
        if (ActStackHelper.isMainProcess(this)) {
            //设置Host
            settingHost()
            //设置网络框架
            NetMgr.getInstance().registerProvider(TonNetProvider())
            //设置字体不跟随系统变化
            setTextSize()
            //设置分享
//            SocialHelper.initSDK(this)
            //rxjava全局异常处理 https://juejin.im/post/5ecc10626fb9a047e25d5aac
            RxJavaPlugins.setErrorHandler { it.printStackTrace() }

            initDefaultPicker()
        }
    }

    private fun setTextSize() {
        val config = Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun settingHost() {

    }

    private fun initDefaultPicker(){
        // 利用修改静态默认属性值，快速定制一套满足自己app样式需求的Picker.
        // BasePickerView
        PickerView.sDefaultVisibleItemCount = 5  //item可见个数
        PickerView.sDefaultItemSize = 44  //默认itemSize
        PickerView.sDefaultIsCirculation = false //默认是否循环

        // PickerView 中间文字的样式
        PickerView.sOutTextSize = 14
        PickerView.sCenterTextSize = 18
        PickerView.sCenterColor = Color.parseColor("#5c96f0")
        PickerView.sOutColor = Color.parseColor("#919191")

        // BasePicker
        var padding = 0
        BasePicker.sDefaultPaddingRect = Rect(padding, padding, padding, padding)
        BasePicker.sDefaultPickerBackgroundColor = Color.WHITE
        // 自定义弹窗
        BasePicker.sDefaultDialogCreator = IGlobalDialogCreator { context ->
            DefaultPickerDialog(context)
        }

        // DefaultCenterDecoration 中间线条的样式
        DefaultCenterDecoration.sDefaultLineWidth = 0.5F
        DefaultCenterDecoration.sDefaultLineColor = Color.parseColor("#e5e5e5")
        //DefaultCenterDecoration.sDefaultDrawable = new ColorDrawable(Color.WHITE);
        var leftMargin = 0
        var topMargin = 0
        DefaultCenterDecoration.sDefaultMarginRect = Rect(leftMargin, - topMargin, leftMargin, - topMargin)
    }

}