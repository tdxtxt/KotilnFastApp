package com.fastdev.ui.activity.main.child

import androidx.fragment.app.FragmentPagerAdapter
import com.baselib.helper.LogA
import com.baselib.helper.StatusBarHelper
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_menu1.*

class OneMenuFragment : BaseFragment() {
    private val fragments = listOf<Pair<String, BaseFragment?>>(
            Pair("Tab1", newInstance(TwoMenuFragment::class.java)),
            Pair("Tab2", newInstance(TwoMenuFragment::class.java)),
            Pair("Tab3", newInstance(TwoMenuFragment::class.java)),
            Pair("Tab4", newInstance(TwoMenuFragment::class.java))
    )

    override fun getLayoutId() = R.layout.fragment_main_menu1

    override fun initUi() {
        StatusBarHelper.setStatusBarHeight(fragmentActivity, tabLayout)
        LogA.i("menu1 initUI")
        tabLayout.setupWithViewPager(viewpager, false)
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewpager.adapter = object : FragmentPagerAdapter(fragmentActivity.supportFragmentManager){
            override fun getItem(position: Int) = fragments[position].second!!
            override fun getCount() = fragments.size
            override fun getPageTitle(position: Int) = fragments[position].first
        }

    }
}