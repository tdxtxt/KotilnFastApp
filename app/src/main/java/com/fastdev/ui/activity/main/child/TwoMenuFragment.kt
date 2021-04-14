package com.fastdev.ui.activity.main.child

import android.content.Intent
import com.baselib.helper.LogA
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.jz.videoplayer.ListViewToDetailActivity
import kotlinx.android.synthetic.main.fragment_main_menu2.*

class TwoMenuFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_main_menu2

    override fun initUi() {
        LogA.i("menu2 initUI")
        tv_menu.setOnClickListener {
            startActivity(Intent(activity, ListViewToDetailActivity::class.java))
        }
    }
}