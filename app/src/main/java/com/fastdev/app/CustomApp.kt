package com.fastdev.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.baselib.app.ApplicationDelegate
import com.baselib.helper.ActStackHelper
import com.baselib.net.NetMgr
import com.fast.libdeveloper.AppContainer
import com.fastdev.Flavor
import com.fastdev.helper.ShareSdk
import com.fastdev.net.config.TonNetProvider
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import org.litepal.LitePal

/**
 * 创建时间： 2020/5/27
 * 编码： tangdex
 * 功能描述:
 * https://guolin.blog.csdn.net/article/details/109787732
 */
@HiltAndroidApp
class CustomApp : Application() {
    lateinit var mApplicationDelegate: ApplicationDelegate

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        mApplicationDelegate = object : ApplicationDelegate(this@CustomApp){
            override fun isLoggable() = true
            override fun getAppContainer() = Flavor.createAppContainer(base)
        }
        mApplicationDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (ActStackHelper.isMainProcess(this)) {
            mApplicationDelegate.onCreate()
            //设置Host
            settingHost()
            //设置网络框架
            NetMgr.getInstance().registerProvider(TonNetProvider())
            //设置字体不跟随系统变化
//            setTextSize()
            //设置分享
//            ShareSdk.init(this)
            //rxjava全局异常处理 https://juejin.im/post/5ecc10626fb9a047e25d5aac
            RxJavaPlugins.setErrorHandler { it.printStackTrace() }
            //数据库
            LitePal.initialize(this)
        }
    }

    private fun setTextSize() {
        val config = Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun settingHost() {

    }

}