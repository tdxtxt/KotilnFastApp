package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.baselib.helper.CommonCacheHelper
import com.baselib.helper.DialogHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.activity.CommToolBarActivity
import com.baselib.ui.fragment.BaseFragment
import com.baselib.ui.view.other.TextSpanController
import com.fastdev.helper.clearLogin
import com.fastdev.helper.getAccountNo
import com.fastdev.helper.isLogin
import com.fastdev.ui.R
import com.fastdev.ui.activity.login.LoginMainActivity
import com.fastdev.ui.activity.task.fragment.TaskListFragment
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import com.fastdev.ui.dialog.ConfigDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_task_list.*
import org.litepal.LitePal
import org.litepal.LitePalDB

@AndroidEntryPoint
class TaskListActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.activity_task_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(TextUtils.isEmpty(CommonCacheHelper.getAccountNo())){
            DialogHelper.showCommDialog(fragmentActivity, "警告", "未获取到登录信息", {
                menuText = "退出"
            }){
                menuText = "重新登录"
                onClick { rootView, any ->
                    CommonCacheHelper.clearLogin()
                    LoginMainActivity.open(fragmentActivity, {
                        recreate()
                    }){
                        finish()
                    }
                }
            }
        }else{
            //初始化数据库
            val db = LitePalDB(CommonCacheHelper.getAccountNo(), 1)
            LitePal.use(db)
        }
    }

    override fun initUi() {
        setTitleBar("资产盘点"){
            menuText = TextSpanController().pushColorSpan(Color.parseColor("#666666")).append("参数设置").popSpan().build()
            onClick { rootView, any ->
                ConfigDialog(fragmentActivity).show()
//                ToastHelper.showToast("参数设置")
            }
        }
        val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()

        fragments.add(Pair("进行中", TaskListFragment.getIngInstance()))
        fragments.add(Pair("已结束", TaskListFragment.getEndInstance()))

        viewPager.setAdapter(BaseFragmentPagerAdapter(fragments, supportFragmentManager))
        tabLayout.setViewPager(viewPager)

        viewPager.setCurrentItem(0)
        tabLayout.onPageSelected(0)
    }

    override fun clickTitleBarBack(): Boolean {
        DialogHelper.showCommDialog(fragmentActivity, "温馨提示", "确定退出当前登录账户吗？", {
            menuText = "取消"
        }){
            menuText = "确定"
            onClick { rootView, any ->
                CommonCacheHelper.clearLogin()
                LoginMainActivity.open(fragmentActivity, {
                    recreate()
                }){
                    finish()
                }
            }
        }
        return false
    }

    companion object{
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, TaskListActivity::class.java))
        }
    }
}
