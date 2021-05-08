package com.fastdev

import android.content.Context
import com.fast.libdeveloper.AppContainer
import com.fastdev.ui.BuildConfig

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/30
 */
object Flavor {
    fun createAppContainer(context: Context): AppContainer {
        return AppContainer.DEFAULT
    }

    fun createBaseUrl() = BuildConfig.HOST1_RELEASE

}