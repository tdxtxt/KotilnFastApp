package com.fastdev.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.baselib.helper.FragmentHelper
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.main.child.HomeFragment
import com.fastdev.ui.activity.main.child.MineFragment
import com.fastdev.ui.activity.main.child.OneMenuFragment
import com.fastdev.ui.activity.main.child.TwoMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/6/17
 */
@AndroidEntryPoint
class MainActivity : BaseActivity(){
    override fun getLayoutResId() = R.layout.activity_main
    private lateinit var currentTab: String

    private val mFragmentPages = mutableMapOf<String, BaseFragment?>()

    override fun getParams(bundle: Bundle?) {
        currentTab = bundle?.getString("tab", TAB_HOME)?: TAB_HOME
    }

    override fun initUi() {
        mFragmentPages[TAB_HOME] = BaseFragment.newInstance(HomeFragment::class.java)
        mFragmentPages[TAB_MENU1] = BaseFragment.newInstance(OneMenuFragment::class.java)
        mFragmentPages[TAB_MENU2] = BaseFragment.newInstance(TwoMenuFragment::class.java)
        mFragmentPages[TAB_MINE] = BaseFragment.newInstance(MineFragment::class.java)

        nav_bottom.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.menu_home -> navFragment(TAB_HOME).run { true }
                R.id.menu_dynamic -> navFragment(TAB_MENU1).run { true }
                R.id.menu_college -> navFragment(TAB_MENU2).run { true }
                R.id.menu_mine -> navFragment(TAB_MINE).run { true }
                else -> false
            }
        }
        nav_bottom.itemIconTintList = null
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
            FragmentHelper.showHide(displayFragment, FragmentHelper.getFragments(supportFragmentManager))
        }
        currentTab = tab
    }

    companion object{
        private const val TAB_HOME = "tab_home"
        private const val TAB_MENU1 = "tab_1"
        private const val TAB_MENU2 = "tab_2"
        private const val TAB_MINE = "tab_mine"

        fun showHomePage(activity: Activity?){
            activity?.startActivity(
                    Intent(activity, MainActivity::class.java)
                            .putExtra("tab", TAB_HOME)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
    }

}