package com.fastdev.ui.activity.welcome;

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.baselib.helper.*
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.net.ApiClient
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.child.AdFragment
import com.fastdev.ui.activity.welcome.child.GuideFragment
import com.fastdev.ui.activity.welcome.child.SplashFragment
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * 功能描述:
 * @author tangdexiang
 * 流程 https://github.com/zonezoen/ForGitHubProject
 * @since 2020/6/11
 */
class WelcomeActivity : BaseActivity() {
    private val countDownTime = 3L//倒计时时间

    override fun getLayoutResId() = R.layout.act_welcome

    var isBack = false
    //返回按键拦截
    override fun interceptBackEvent() = !isBack

    override fun onCreate(savedInstanceState: Bundle?) {
        //给Preview Window设置的背景图如果不做处理，图片就会一直存在于内存中，所以，当我们进入到欢迎页的时候要进行清空
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
        BaseFragment.newInstance(SplashFragment::class.java)?.apply {
            FragmentHelper.replace(supportFragmentManager, this, R.id.view_content)
        }
    }

    override fun initStatusBar() {
        StatusBarHelper.transparentStatusBar(activity)
        StatusBarHelper.setDarkMode(activity)
    }

    @SuppressLint("CheckResult")
    override fun initUi() {
        if(NetworkHelper.isConnected()){//有网络
            //请求广告接口获取广告图片,是否显示广告
            ApiClient.getService().testApi2("xxx").composeBindLifecycle(this).subscribe {}
        }
        //倒计时3秒
        Flowable.interval(0, 1, TimeUnit.SECONDS)//延迟0，间隔1s，单位秒
                .take(countDownTime + 1)//限制发射次数（因为倒计时要显示 3 2 1 0 四个数字）
                //使用map将数字转换，这里aLong是从0开始增长的,所以减去aLong就会输出3 2 1 0这种样式
                .map { countDownTime - it }
                .composeBindLifecycle(this)
                .subscribe({}, {}){
                    isBack = true
                    if(GuideFragment.isNewVersion()){ //是否启动过引导页
                        BaseFragment.newInstance(GuideFragment::class.java)?.apply {  FragmentHelper.replace(supportFragmentManager, this, R.id.view_content) }
                    }else{
                        BaseFragment.newInstance(AdFragment::class.java)?.apply {  FragmentHelper.replace(supportFragmentManager, this, R.id.view_content) }
                    }
                }
    }
}
