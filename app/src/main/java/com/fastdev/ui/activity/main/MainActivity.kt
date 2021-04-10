package com.fastdev.ui.activity.main

import android.text.TextUtils
import com.baselib.helper.FragmentHelper
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.main.child.HomeFragment
import com.fastdev.ui.activity.main.child.MineFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/6/17
 */
class MainActivity : BaseActivity(){
    override fun getLayoutResId() = R.layout.activity_main
    private val TAB_HOME = "tab_home"
    private val TAB_MENU1 = "tab_1"
    private val TAB_MENU2 = "tab_2"
    private val TAB_MINE = "tab_mine"
    private var currentTab = TAB_HOME

    val mFragmentPages = mutableMapOf<String, BaseFragment?>()

    override fun initUi() {
        mFragmentPages[TAB_HOME] = BaseFragment.newInstance(HomeFragment::class.java)
        mFragmentPages[TAB_MENU1] = BaseFragment.newInstance(MineFragment::class.java)
        mFragmentPages[TAB_MENU2] = BaseFragment.newInstance(HomeFragment::class.java)
        mFragmentPages[TAB_MINE] = BaseFragment.newInstance(MineFragment::class.java)

        FragmentHelper.add(supportFragmentManager, mFragmentPages[TAB_HOME]!!, R.id.view_content, true)
        FragmentHelper.add(supportFragmentManager, mFragmentPages[TAB_MENU1]!!, R.id.view_content, true)
        FragmentHelper.add(supportFragmentManager, mFragmentPages[TAB_MENU2]!!, R.id.view_content, true)
        FragmentHelper.add(supportFragmentManager, mFragmentPages[TAB_MINE]!!, R.id.view_content, true)

        nav_bottom.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.menu_home -> navFragment(TAB_HOME).run { true }
                R.id.menu_1 -> navFragment(TAB_MENU1).run { true }
                R.id.menu_2 -> navFragment(TAB_MENU2).run { true }
                R.id.menu_mine -> navFragment(TAB_MINE).run { true }
                else -> false
            }
        }
        nav_bottom.selectedItemId = R.id.menu_home
    }

    private fun navFragment(tab: String){
        val displayFragment = mFragmentPages[tab] ?: return
        val oldFragment = FragmentHelper.findFragment(supportFragmentManager, displayFragment.javaClass)
        if(oldFragment == null){
            FragmentHelper.add(supportFragmentManager, displayFragment, R.id.view_content)
        }
        if(TextUtils.isEmpty(currentTab)){
            FragmentHelper.show(displayFragment)
        }else if(tab != currentTab){
            FragmentHelper.showHide(displayFragment, mFragmentPages[currentTab])
        }
        currentTab = tab
    }

}