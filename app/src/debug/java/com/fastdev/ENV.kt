package com.fastdev

import com.fastdev.ui.BuildConfig

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/5/6
 */
class ENV constructor(val name: String, val host: String) {
    companion object {
        val values: List<ENV> by lazy {
            listOf(ENV("开发环境", BuildConfig.HOST1_DEVE), ENV("测试环境", BuildConfig.HOST1_TEST), ENV("生产环境", BuildConfig.HOST1_RELEASE), ENV("自定义", ""))
        }

        fun isCustom(host: String) = host != BuildConfig.HOST1_DEVE && host != BuildConfig.HOST1_TEST && host != BuildConfig.HOST1_RELEASE

        fun isDevEnv(host: String) = host == BuildConfig.HOST1_DEVE

        fun isReleaseEnv(host: String) = host == BuildConfig.HOST1_RELEASE

        fun isRelEnv(host: String) = host == BuildConfig.HOST1_RELEASE_PRE

        fun from(index: Int) = values[index]
    }
}