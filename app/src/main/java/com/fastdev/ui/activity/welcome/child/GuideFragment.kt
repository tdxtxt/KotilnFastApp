package com.fastdev.ui.activity.welcome.child

import com.baselib.helper.CacheHelper
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.helper.getGuideVersion
import com.fastdev.helper.putGuideVersion
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/3
 */
class GuideFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_guide

    override fun initUi() {
        CacheHelper.putGuideVersion(GuideFragment.versionCode)
    }

    companion object {
        const val versionCode = "1.0.0"

        fun isNewVersion() = GuideFragment.versionCode == CacheHelper.getGuideVersion()
    }
}