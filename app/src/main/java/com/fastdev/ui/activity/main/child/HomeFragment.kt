package com.fastdev.ui.activity.main.child

import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/10
 */
class HomeFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_main_home

    override fun initUi() {
        LogA.i("home initUI")
    }
}