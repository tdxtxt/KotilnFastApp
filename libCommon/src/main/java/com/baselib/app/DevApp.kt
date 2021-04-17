package com.baselib.app

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import androidx.multidex.MultiDex
import com.baselib.R
import com.baselib.helper.CacheHelper
import com.baselib.helper.LogA
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import org.jaaksi.pickerview.dialog.DefaultPickerDialog
import org.jaaksi.pickerview.dialog.IGlobalDialogCreator
import org.jaaksi.pickerview.picker.BasePicker
import org.jaaksi.pickerview.widget.DefaultCenterDecoration
import org.jaaksi.pickerview.widget.PickerView


/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 需在application中调用onCreate方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
open abstract class DevApp : Application(){

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        closeAndroidPDialog()
        LogA.init(isLoggable())
        CacheHelper.init()
        ForegroundCallbacks.init(this)
                .addListener(object : ForegroundListener{
                    override fun onBecameForeground() {
                    }
                    override fun onBecameBackground() {
                    }
                })

        initDefaultPicker()
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
        val padding = 0
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
        val leftMargin = 0
        val topMargin = 0
        DefaultCenterDecoration.sDefaultMarginRect = Rect(leftMargin, - topMargin, leftMargin, - topMargin)
    }

    /**
     * 消除Android P上的提醒弹窗 （Detected problems with API compatibility(visit g.co/dev/appcompat for more info)
     */
    private fun closeAndroidPDialog(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        try {
            val aClass = Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
            declaredConstructor.setAccessible(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod =
                    cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown =
                    cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun isLoggable(): Boolean

    companion object {
        var mContext: Context ? = null
        fun getContext(): Context? {
            return mContext
        }

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setEnableHeaderTranslationContent(false)
                MaterialHeader(context).setColorSchemeResources(R.color.red_dc143c,R.color.green_008000,R.color.blue_064fc3_dark)
//                layout.setPrimaryColorsId(R.color.black_333333, R.color.white_ffffff) //全局设置主题颜色
//                ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}