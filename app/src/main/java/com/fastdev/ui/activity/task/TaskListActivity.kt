package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment
import com.baselib.helper.ToastHelper
import com.baselib.ui.activity.CommToolBarActivity
import com.baselib.ui.fragment.BaseFragment
import com.baselib.ui.view.other.TextSpanController
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.fragment.TaskListFragment
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : CommToolBarActivity() {
    val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()

    override fun getLayoutResId() = R.layout.activity_task_list

    override fun initUi() {
        setTitleBar("资产盘点"){
            menuText = TextSpanController().pushColorSpan(Color.parseColor("#666666")).append("参数设置").popSpan().build()
            onClick { rootView, any ->
                ToastHelper.showToast("参数设置")
            }
        }

        fragments.add(Pair("进行中", TaskListFragment.getIngInstance()))

        fragments.add(Pair("已结束", TaskListFragment.getEndInstance()))

        viewPager.setAdapter(BaseFragmentPagerAdapter(fragments, supportFragmentManager))
        tabLayout.setViewPager(viewPager)

        viewPager.setCurrentItem(0)
        tabLayout.onPageSelected(0)
    }

    companion object{
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, TaskListActivity::class.java))
        }
    }
}
