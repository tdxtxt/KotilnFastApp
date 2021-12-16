package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.fragment.SourceListFragment
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : CommToolBarActivity() {
    val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()

    override fun getLayoutResId() = R.layout.activity_task_details

    override fun initUi() {
        setTitleBar("盘点任务详情")

        fragments.add(Pair("全部", SourceListFragment.getAllInstance()))
        fragments.add(Pair("待盘", SourceListFragment.getUnInstance()))
        fragments.add(Pair("已盘", SourceListFragment.getCompleteInstance()))
        fragments.add(Pair("盘盈", SourceListFragment.getOutsideInstance()))
        fragments.add(Pair("盘亏", SourceListFragment.getWaneInstance()))

        viewPager.setAdapter(BaseFragmentPagerAdapter(fragments, supportFragmentManager))
        tabLayout.setViewPager(viewPager)

        viewPager.setCurrentItem(0)
        tabLayout.onPageSelected(0)
    }

    companion object{
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, TaskDetailsActivity::class.java))
        }
    }
}
