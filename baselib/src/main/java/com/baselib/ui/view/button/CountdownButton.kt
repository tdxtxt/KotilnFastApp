package com.baselib.ui.view.button

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.baselib.R

/**
 * 功能描述: 发送短信倒计时控件
 * https://hub.fastgit.org/lindroy/CountdownButton
 * @author tangdexiang
 * @since 2020/8/5
 */
class CountdownButton : AppCompatButton {
    /**倒计时的总时间**/
    var millisInFuture = 2 * 60 * 1000
    /**倒计时的时间间隔**/
    var countDownInterval = 1 * 1000
    /**倒计时进行中的按钮文字**/
    var tickText = ""
    /**倒计时结束时的按钮文字**/
    var finishText = ""
    /**倒计时是否在进行**/
    private var isTicking = false
    /**倒计时是否要开始**/
    private var isStarted = false
    private var countdownTimer: CountDownTimer? = null
    /**正常按钮文字**/
    private var buttonText = ""

    private var finishedListener: (() -> Unit)? = null

    private var tickListener: ((interval: Int) -> Unit)? = null

    private var startListener: (() -> Unit)? = null

    private var cancelListener: (() -> Unit)? = null

    private var countdownListener: OnCountdownListener? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CountdownButton,
            defStyleAttr,
            0
        )
        typedArray?.let {
            tickText = it.getString(R.styleable.CountdownButton_tickText)
                ?: context.getString(R.string.countdown_button_tick_text)
            finishText = it.getString(R.styleable.CountdownButton_finishedText)
                ?: context.getString(R.string.countdown_button_finished_text)
            millisInFuture = it.getInt(R.styleable.CountdownButton_millisInFuture, millisInFuture)
            countDownInterval = it.getInt(R.styleable.CountdownButton_countDownInterval, countDownInterval)
            it.recycle()
        }
        //取消全部英文字母大写
        isAllCaps = false
    }

    /**
     * 启动倒计时
     */
    fun start() {
        isStarted = true
        updateCounting()
    }

    /**
     * 取消倒计时
     */
    fun cancel() {
        isStarted = false
        updateCounting()
    }

    private fun updateCounting() {
        if (isTicking == isStarted) {
            return
        }
        when (isStarted) {
            //倒计时开启
            true -> {
                isEnabled = false
                buttonText = text.toString()
                if (countdownTimer == null) {
                    countdownTimer = object : CountDownTimer(
                        (millisInFuture - 1000).toLong() + 122, //millisInFuture-1000是为了跳过0秒
                        countDownInterval.toLong()
                    ) {
                        /**
                         * Callback fired on regular interval.
                         * @param millisUntilFinished The amount of time until finished.
                         */
                        override fun onTick(millisUntilFinished: Long) {
                            //转换成double类型后再除于1000，是为了得到小数，避免出现漏秒的情况
                            val time = (Math.round(millisUntilFinished.toDouble() / 1000)).toInt()
                            text = String.format(tickText, time)
                            onTick(time)
                        }

                        /**
                         * Callback fired when the time is up.
                         */
                        override fun onFinish() {
                            //倒计时结束
                            isTicking = false
                            isEnabled = true
                            text = finishText
                            onFinished()
                        }
                    }.start()
                } else {
                    countdownTimer?.start()
                }
                onStart()
            }
            //倒计时结束
            false -> {
                countdownTimer?.cancel()
                isEnabled = true
//                text = buttonText
                text = finishText
                onCancel()
            }
        }
        isTicking = isStarted
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancel()
        startListener = null
        cancelListener = null
        finishedListener = null
        tickListener = null
        countdownListener = null
    }


    private fun onStart() {
        startListener?.invoke()
        countdownListener?.onStart()
    }

    private fun onCancel() {
        cancelListener?.invoke()
        countdownListener?.onCancel()
    }

    private fun onTick(interval: Int) {
        tickListener?.invoke(interval)
        countdownListener?.onTick(interval)
    }

    private fun onFinished() {
        finishedListener?.invoke()
        countdownListener?.onFinished()
    }

    /**
     * 倒计时完成的监听
     */
    fun setOnFinishedListener(listener: () -> Unit) {
        finishedListener = listener
    }

    /**
     * 倒计时进行中的监听
     * @param listener:参数interval为经过优化处理的整数
     */
    fun setOnTickListener(listener: (interval: Int) -> Unit) {
        tickListener = listener
    }

    /**
     * 开始倒计时的监听
     */
    fun setOnStartListener(listener: () -> Unit) {
        startListener = listener
    }

    /**
     * 取消倒计时的监听
     */
    fun setonCancelListener(listener: () -> Unit) {
        cancelListener = listener

    }

    /**
     * 设置倒计时监听事件
     */
    fun setOnCountdownListener(listener: OnCountdownListener) {
        countdownListener = listener
    }

    /**
     * 倒计时监听接口
     */
    interface OnCountdownListener {
        /**
         * 倒计时开始
         */
        fun onStart()

        /**
         * 倒计时取消
         */
        fun onCancel()

        /**
         * 倒计时进行中
         */
        fun onTick(interval: Int)

        /**
         * 倒计时结束
         */
        fun onFinished()
    }

    /**
     * OnCountdownListener实现类，可用于添加单个监听事件
     */
    open class SimpleOnCountdownListener : OnCountdownListener {
        /**
         * 倒计时开始
         */
        override fun onStart() {
        }

        /**
         * 倒计时取消
         */
        override fun onCancel() {
        }

        /**
         * 倒计时进行中
         */
        override fun onTick(interval: Int) {
        }

        /**
         * 倒计时结束
         */
        override fun onFinished() {
        }
    }

}