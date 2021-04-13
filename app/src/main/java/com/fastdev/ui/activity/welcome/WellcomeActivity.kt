package com.fastdev.ui.activity.welcome;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.baselib.helper.*
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.data.repository.TestRepository
import com.fastdev.ui.R
import com.fastdev.ui.activity.main.MainActivity
import com.fastdev.ui.activity.welcome.child.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * 流程 https://github.com/zonezoen/ForGitHubProject
 * @since 2020/6/11
 */
@AndroidEntryPoint
class WellcomeActivity : BaseActivity() {
    @Inject
    lateinit var testApi: TestRepository

    private val countDownTime = 1L//显示闪屏页1秒

    override fun getLayoutResId() = R.layout.activity_wellcome

    override fun onCreate(savedInstanceState: Bundle?) {
        //给Preview Window设置的背景图如果不做处理，图片就会一直存在于内存中，所以，当我们进入到欢迎页的时候要进行清空
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
    }

    override fun initStatusBar() {
        StatusBarHelper.transparentStatusBar(activity)
        StatusBarHelper.setDarkMode(activity)
    }

    @SuppressLint("CheckResult")
    override fun initUi() {
        if(NetworkHelper.isConnected()){//有网络
            //请求广告接口获取广告图片,是否显示广告
//            testApi.queryList1().composeBindLifecycle(this).subscribe()
        }
        //延迟时1秒
        Flowable.timer(countDownTime, TimeUnit.SECONDS)
                .subscribe { handleResult() }
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
        val intent = intent.apply {
            setClass(activity, MainActivity::class.java)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        startActivity(intent)
        finish()
    }
}
