package com.fastdev.ui.activity.welcome;

import android.content.Intent
import android.os.Bundle
import com.baselib.helper.*
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.helper.isLogin
import com.fastdev.ui.R
import com.fastdev.ui.activity.login.LoginMainActivity
import com.fastdev.ui.activity.main.MainActivity
import com.fastdev.ui.activity.task.TaskListActivity
import com.fastdev.ui.activity.welcome.child.AdFragment
import com.fastdev.ui.activity.welcome.child.GuideFragment
import com.fastdev.ui.activity.welcome.child.PermissionApplyFragment
import com.fastdev.ui.activity.welcome.child.PrivacyFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 功能描述:
 * @author tangdexiang
 * 流程 https://github.com/zonezoen/ForGitHubProject
 * @since 2020/6/11
 */
@AndroidEntryPoint
class WellcomeActivity : BaseActivity() {
    override fun getLayoutResId() = R.layout.activity_wellcome

    override fun onCreate(savedInstanceState: Bundle?) {
        //给Preview Window设置的背景图如果不做处理，图片就会一直存在于内存中，所以，当我们进入到欢迎页的时候要进行清空
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
    }

    override fun initStatusBar() {
        StatusBarHelper.transparentStatusBar(fragmentActivity)
        StatusBarHelper.setDarkMode(fragmentActivity)
    }

    override fun initUi() {
        startMain()
    }

    fun handleResult(){
        if(!PrivacyFragment.isPrivacyDisplay){ //显示隐私协议
            BaseFragment.newInstance(PrivacyFragment::class.java)?.apply {
                FragmentHelper.replace(supportFragmentManager, this, R.id.view_content)
            }
        }else if(!PermissionApplyFragment.isPermissionDisplay){ //显示获取权限
            BaseFragment.newInstance(PermissionApplyFragment::class.java)?.apply {
                FragmentHelper.replace(supportFragmentManager, this, R.id.view_content)
            }
        }else if(GuideFragment.isNewVersion()){ //是否启动过引导页
            BaseFragment.newInstance(GuideFragment::class.java)?.apply {
                FragmentHelper.replace(supportFragmentManager, this, R.id.view_content)
            }
        }else if(!AdFragment.isAdDisplay){ // 广告
            BaseFragment.newInstance(AdFragment::class.java)?.apply {
                FragmentHelper.replace(supportFragmentManager, this, R.id.view_content)
            }
        }
        else{
            startMain()
        }
    }

    fun startMain(){
        if(CommonCacheHelper.isLogin()){
            TaskListActivity.open(fragmentActivity)
            finish()
        }else{
            LoginMainActivity.open(fragmentActivity, {
                TaskListActivity.open(fragmentActivity)
                finish()
            }){
                finish()
            }
        }
    }
}
