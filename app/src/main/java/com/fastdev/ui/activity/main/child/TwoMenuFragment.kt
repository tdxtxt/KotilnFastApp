package com.fastdev.ui.activity.main.child

import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R

class TwoMenuFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_main_menu2

    override fun initUi() {
        LogA.i("menu2 initUI")
    }
}