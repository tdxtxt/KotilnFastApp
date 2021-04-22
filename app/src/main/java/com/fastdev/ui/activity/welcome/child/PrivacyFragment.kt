package com.fastdev.ui.activity.welcome.child

import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.WellcomeActivity
import kotlinx.android.synthetic.main.fragment_wellcome_privacy.*

class PrivacyFragment : BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_wellcome_privacy

    override fun initUi() {
        listOf(tv_privacy_user, tv_privacy_ys, tv_next).forEach {
                    it.setOnClickListener {
                        when(it){
                            tv_next -> {
                                isPrivacyDisplay = true
                                getActivityNew<WellcomeActivity>()?.handleResult()
                            }
                        }
                    }
                }
    }

    companion object{
        var isPrivacyDisplay = false
    }
}