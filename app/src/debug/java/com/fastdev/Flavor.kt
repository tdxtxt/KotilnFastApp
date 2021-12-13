package com.fastdev

import android.content.Context
import com.baselib.app.ApplicationDelegate
import com.fast.libdeveloper.*
import com.fastdev.net.ApiClient

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/30
 */
object Flavor {
    val ENDPOINT = object : Endpoint {
        override fun isMock(index: Int) = false

        override fun count(): Int {
            return ENV.values.size
        }

        override fun url(index: Int): String {
            return ENV.from(index).host
        }

        override fun isCustom(index: Int): Boolean {
            return ENV.isCustom(ENV.from(index).host)
        }

        override fun extraUrls(index: Int): MutableList<ExtraUrl> {
            return mutableListOf()
        }

        override fun changeIndex(index: Int) {
            if(ENV.isDevEnv(url(index))){

            }else if(ENV.isRelEnv(url(index))){

            }else if(ENV.isReleaseEnv(url(index))){

            }else if(ENV.isCustom(url(index))){

            }

            ApiClient.changeHost(url(index))
        }

        override fun name(index: Int): String {
            return ENV.from(index).name
        }
    }

    fun createAppContainer(context: Context): AppContainer {
        return DebugAppContainer.getInstance(context, ENDPOINT)
    }

    fun createBaseUrl() = DebugAppContainer.getInstance(ApplicationDelegate.context, ENDPOINT).debugEnvironment().url

}