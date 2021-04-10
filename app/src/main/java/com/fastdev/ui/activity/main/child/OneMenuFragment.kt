package com.fastdev.ui.activity.main.child

import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R

class OneMenuFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_main_menu1

    override fun initUi() {
        LogA.i("menu1 initUI")
    }
}