package com.fastdev.ui.activity.main.child

import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.huanxin.chat.ChatActivity
import kotlinx.android.synthetic.main.fragment_main_menu1.*

class OneMenuFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_main_menu1

    override fun initUi() {
        LogA.i("menu1 initUI")

        tv_menu1.setOnClickListener {
            ChatActivity.open(activity, "2323")
        }
    }
}