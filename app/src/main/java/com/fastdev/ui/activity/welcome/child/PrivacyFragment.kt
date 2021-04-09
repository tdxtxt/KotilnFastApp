package com.fastdev.ui.activity.welcome.child

import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.WellcomeActivity
import kotlinx.android.synthetic.main.fragment_privacy.*

class PrivacyFragment : BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_privacy

    override fun initUi() {
        btn_next.setOnClickListener {
            isPrivacyDisplay = true
            getActivityNew<WellcomeActivity>()?.handleResult()
        }
    }

    companion object{
        var isPrivacyDisplay = false
    }
}