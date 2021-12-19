package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.baselib.helper.LogA
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.core.MonitorProtocol
import com.fastdev.core.UHFSdk
import com.fastdev.ui.R
import com.fastdev.ui.activity.qrcode.ScanQrcodeActivity
import com.fastdev.ui.activity.task.fragment.SourceListFragment
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import com.fastdev.ui.dialog.NewSourceFilterDialog
import com.fastdev.ui.dialog.ScannerDialog
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : CommToolBarActivity() {
    val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()

    override fun getLayoutResId() = R.layout.activity_task_details

    override fun initUi() {
        setTitleBar("盘点任务详情"){
            menuText = "扫一扫"
            onClick { rootView, any ->
                ScanQrcodeActivity.open(fragmentActivity){

                }
            }
        }

        setInterceptBackEvent(false){
            MonitorProtocol.stopReadMonitor()
        }

        listOf(btn_start, btn_end, tv_filter).forEach {
            it.setOnClickListener {
                when(it){
                    btn_start -> {
//                        ScannerDialog(fragmentActivity).show()
                        MonitorProtocol.startReadMonitor()
                    }

                    btn_end -> {
                        MonitorProtocol.stopReadMonitor()
                    }

                    tv_filter -> {
                        NewSourceFilterDialog(fragmentActivity).show()
                    }
                }
            }
        }

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogA.i("keyCode=$keyCode;")
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        LogA.i("keyCode=$keyCode;")
        return super.onKeyUp(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        UHFSdk.resume()
    }

    override fun onPause() {
        super.onPause()
        UHFSdk.pause()
    }

    companion object{
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, TaskDetailsActivity::class.java))
        }
    }
}
