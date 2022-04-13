package com.fastdev.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//半圆环 https://github.com/kong-jing/CircleProgressView
//齿轮线 https://juejin.cn/post/7043705977538871332
//齿轮半圆环 https://juejin.cn/post/6844903777548386318
// https://github.com/HotBitmapGG/CreditSesameRingView
public class StudyTimeChartView extends View {
    public StudyTimeChartView(Context context) {
        super(context);
    }

    public StudyTimeChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heitht = width / 2 / 4 * 5;
//        initIndex(width / 2);
        //优化组件高度
        setMeasuredDimension(width, heitht);
    }
}
