package com.fastdev.ui.activity.main.child

import androidx.fragment.app.FragmentPagerAdapter
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_menu2.tabLayout
import kotlinx.android.synthetic.main.fragment_main_menu2.viewpager

class TwoMenuFragment : BaseFragment() {
    private val fragments = listOf<Pair<String, BaseFragment?>>(
            Pair("TabA", newInstance(TestFragment::class.java)),
            Pair("TabB", newInstance(TestFragment::class.java)),
            Pair("TabC", newInstance(TestFragment::class.java)),
            Pair("TabD", newInstance(TestFragment::class.java))
    )

    override fun getLayoutId() = R.layout.fragment_main_menu2

    override fun initUi() {
        tabLayout.setupWithViewPager(viewpager)
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewpager.adapter = object : FragmentPagerAdapter(fragmentActivity!!.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getItem(position: Int) = fragments[position].second!!
            override fun getCount() = fragments.size
            override fun getPageTitle(position: Int) = fragments[position].first
        }
    }
}