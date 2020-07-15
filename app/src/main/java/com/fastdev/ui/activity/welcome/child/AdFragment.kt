package com.fastdev.ui.activity.welcome.child

import android.annotation.SuppressLint
import android.content.Intent
import com.baselib.helper.composeBindLifecycle
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.test.MainActivity
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_ad.*
import java.util.concurrent.TimeUnit

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/3
 */
class AdFragment : BaseFragment() {
    private val countDownTime = 3L//倒计时时间
    override fun getLayoutId() = R.layout.fragment_ad

    @SuppressLint("CheckResult")
    override fun initUi() {
        //倒计时3秒
        Flowable.interval(0, 1, TimeUnit.SECONDS)//延迟0，间隔1s，单位秒
                .take(countDownTime + 1)//限制发射次数（因为倒计时要显示 3 2 1 0 四个数字）
                //使用map将数字转换，这里aLong是从0开始增长的,所以减去aLong就会输出3 2 1 0这种样式
                .map { countDownTime - it }
                .composeBindLifecycle(this)
                .subscribe({
                    tv_countdown.text = String.format("跳过%d", it)
                }, {}){
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
    }
}