package com.baselib.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.baselib.R;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;


/**
 * 功能描述:
 *
 * @author tangdexiang
 * @since 2020/7/14
 */
public class StatusBarHelper {

    /**
     * 设置入侵到状态栏view的新增高度
     */
    public static void setStatusBarHeight(Context context, View view) {
        if(view == null) return;
        if (view instanceof ViewGroup) {
            view.setPadding(view.getPaddingLeft(),
                    view.getPaddingTop() + getStatusBarHeight(context), view.getPaddingRight(),
                    view.getPaddingBottom());
        } else {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = getStatusBarHeight(context);
            view.setLayoutParams(layoutParams);
        }
    }


    /**
     * 设置状态栏透明【内容全入侵】
     */
    public static void transparentStatusBar(Activity activity) {
        if(activity == null) return;
        ImmersionBar.with(activity)
                .transparentStatusBar()
                .init();
    }

    /**
     * 设置状态栏图标白色主题【白色文字】
     */
    public static void setLightMode(Activity activity) {
        if(activity == null) return;
        ImmersionBar.with(activity)
                .statusBarDarkFont(false)
                .init();
    }

    /**
     * 设置状态栏图片黑色主题【黑色文字】
     */
    public static void setDarkMode(Activity activity) {
        if(activity == null) return;
        ImmersionBar.with(activity)
                .statusBarColor(R.color.white_ffffff) //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    /**
     * 隐藏导航栏和状态栏并全屏
     */
    public static void hideBarAndFullScreen(Activity activity){
        if(activity == null) return;
        ImmersionBar.with(activity)
                .reset()
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .fullScreen(true)
                .init();
    }

    /**
     * 隐藏导航栏
     */
    public static void hideNavigationBar(Activity activity){
        if(activity == null) return;
        ImmersionBar.with(activity)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }

    /**
     * 隐藏导航栏
     */
    public static void hideStatusBar(Activity activity){
        if(activity == null) return;
        ImmersionBar.with(activity)
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init();
    }

    /**
     * 设置未黑色背景、白色文字的状态栏
     */
    public static void blackStatusBar(Activity activity){
        ImmersionBar.with(activity)
                .reset()
//                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.black_333333)
                .statusBarColor(R.color.black_333333)
//                .fullScreen(true)
                .init();
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
