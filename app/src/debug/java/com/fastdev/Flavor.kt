package com.fastdev

import android.content.Context
import com.fast.libdeveloper.AppContainer
import com.fast.libdeveloper.DebugAppContainer
import com.fast.libdeveloper.Endpoint
import com.fast.libdeveloper.ExtraUrl

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/30
 */
object Flavor {
    val ENDPOINT = object : Endpoint {
        override fun isMock(index: Int) = false

        override fun count(): Int {
            return 0
        }

        override fun url(index: Int): String {
            return "xxxx"
        }

        override fun isCustom(index: Int): Boolean {
            return false
        }

        override fun extraUrls(index: Int): MutableList<ExtraUrl> {
            return mutableListOf()
        }

        override fun name(index: Int): String {
            return "xx"
        }
    }

    fun createAppContainer(context: Context): AppContainer {
        return DebugAppContainer.getInstance(context, ENDPOINT)
    }

}