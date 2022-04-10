package com.fastdev.ui.activity.welcome.child

import android.annotation.SuppressLint
import com.baselib.helper.ImageLoaderHelper
import com.baselib.helper.composeUIThread
import com.baselib.ui.fragment.BaseFragment
import com.fastdev.ui.R
import com.fastdev.ui.activity.welcome.WellcomeActivity
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_wellcome_ad.*
import java.util.concurrent.TimeUnit

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/3
 */
class AdFragment : BaseFragment() {
    private val countDownTime = 3L//倒计时时间
    override fun getLayoutId() = R.layout.fragment_wellcome_ad

    @SuppressLint("CheckResult")
    override fun initUi() {
        ImageLoaderHelper.loadImage(iv_ad, "https://static.veer.com/veer/static/resources/keyword/2020-02-19/b94a42fb11d64052ae5a9baa25f5370c.jpg?x-oss-process=image/format,webp")
        //倒计时3秒
        val disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)//延迟0，间隔1s，单位秒
//                .take(countDownTime + 1)//限制发射次数（因为倒计时要显示 3 2 1 0 四个数字）
                //使用map将数字转换，这里aLong是从0开始增长的,所以减去aLong就会输出3 2 1 0这种样式
                .map { countDownTime - it }
                .composeUIThread()
                .subscribe {
                    tv_countdown.text = "跳过$it"
                    if(it <= 0) tv_countdown.performClick()
                }

        tv_countdown.setOnClickListener {
            disposable.dispose()
            getActivityNew<WellcomeActivity>()?.startMain()
        }

        getActivityNew<WellcomeActivity>()?.setInterceptBackEvent(false) {
            disposable.dispose()
        }
    }

    companion object{
        var isAdDisplay = true
    }
}