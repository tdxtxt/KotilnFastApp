package com.baselib.ui.activity

import android.os.Bundle
import com.hjq.bar.TitleBar
import com.hjq.bar.OnTitleBarListener
import android.view.View
import com.baselib.R
import com.baselib.ui.activity.callback.TitleClickListener
import com.gyf.immersionbar.ImmersionBar
import com.lxj.statelayout.StateLayout


abstract class CommToolBarActivity : BaseActivity() {
    private var mTitleBar: TitleBar? = null
    private var titleListener: TitleClickListener? = null

    /**
     * 布局中TitleBar控件id默认R.id.titlebar，若自定义id，需要重写此方法
     */
    open fun getToolBarResId() = R.id.titlebar

    open fun getContentViewResId() = R.id.view_content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarListener()
    }

    private fun setTitleBarListener() {
        getTitleBar()?.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {
                if(!interceptBackEvent()) finish()
                titleListener?.onLeftClick(v)
            }
            override fun onTitleClick(v: View) {
                titleListener?.onTitleClick(v)
            }
            override fun onRightClick(v: View) {
                titleListener?.onRightClick(v)
            }
        })

        if(getTitleBar() != null) titleListener?.initCustomClick(getTitleBar()!!)
    }

    protected fun setTitleBarClickListener(listener: TitleClickListener){
        this.titleListener = listener
    }

    /**
     * 获取TitleBar控件
     */
    fun getTitleBar(): TitleBar? {
        if(mTitleBar == null) mTitleBar = findViewById(getToolBarResId())
        return mTitleBar
    }


     override fun initStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.black_666666)//bar背景颜色
                .fitsSystemWindows(true)//解决重叠问题
                .statusBarDarkFont(false)//false字体白色，true字体黑色
                .fullScreen(true)
                .transparentNavigationBar()
                .init()
    }

    override fun initStateView(): StateLayout{
        val content: View = findViewById(getContentViewResId())
        return StateLayout (this)
                .apply {
                    if (content == null) wrap(this@CommToolBarActivity) else wrap(content)
                    showContent()
                }
    }

}