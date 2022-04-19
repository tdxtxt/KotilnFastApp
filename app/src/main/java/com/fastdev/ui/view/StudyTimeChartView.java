package com.fastdev.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//半圆环 https://github.com/kong-jing/CircleProgressView
//齿轮线 https://juejin.cn/post/7043705977538871332
//齿轮半圆环 https://juejin.cn/post/6844903777548386318
// https://github.com/HotBitmapGG/CreditSesameRingView
// https://github.com/clwater/AndroidDashBoard/blob/master/app/src/main/java/clwater/androiddashboard/DashBoard.java

public class StudyTimeChartView extends View {
    private Paint scalePaint, scaleTextPaint , textPaint , progressPaint;
    private RectF rect;
    private int[] gradientProgreess1;  // 圆环渐变色
    private int[] gradientProgreess2;  // 圆环渐变色
    private int progress1 = 60;      // 圆环进度(0-100)
    private int progress2 = 30;      // 圆环进度(0-100)
    private int backGroundColor;    //背景色
    private float radius;          //仪表盘半径
    private float spanWidth ;              //view宽度一半
    private float centerX;
    private float centerY;
    private int loseAngle = 60;          //失去的角度
    private int scaleCount = 10;
    int scaleWidth = 20; //刻度线宽度
    int scaleSpace = 40; //刻度线距离外圆间距


    public StudyTimeChartView(Context context) {
        super(context);
        init();
    }

    public StudyTimeChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / 2 * (1 + Math.cos(Math.toRadians(loseAngle / 2f))));
        //优化组件高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        spanWidth = w / 2;
        radius = spanWidth / 4f * 3;
        centerX = w / 2;
        centerY = h / 2;
    }


    private void init() {
        rect = new RectF();
        textPaint = new Paint();
        scalePaint = new Paint();
        scaleTextPaint = new Paint();

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);    // 只描边，不填充
        progressPaint.setStrokeCap(Paint.Cap.ROUND);   // 设置圆角
        progressPaint.setAntiAlias(true);              // 设置抗锯齿
        progressPaint.setDither(true);                 // 设置抖动
        progressPaint.setStrokeWidth(30);

        backGroundColor = Color.WHITE;

        // 初始化进度圆环渐变色
        gradientProgreess1 = new int[]{Color.parseColor("#95ACFF"), Color.parseColor("#4379FF")};
        gradientProgreess2 = new int[]{Color.parseColor("#A2F3DB"), Color.parseColor("#42D0B6")};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //颜色指示的环
        initRing(canvas);
        //刻度文字
        initScale(canvas);
        //进度条1
        initProgress1(canvas);
        //进度条2
        initProgress2(canvas);
        //提示内容
        initText(canvas);
    }

    private void initProgress1(Canvas canvas){
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f, spanWidth);//平移画布坐标原点

        int mrg = scaleSpace + scaleWidth + 30;
        rect = new RectF(-radius + mrg, -radius + mrg, radius - mrg, radius - mrg);
        progressPaint.setStrokeWidth(30);

        progressPaint.setColor(Color.parseColor("#F5F6FA"));
        canvas.drawArc(rect, 90 + loseAngle / 2f, 360 - loseAngle, false, progressPaint);

        progressPaint.setColor(gradientProgreess1[0]);
        canvas.drawArc(rect, 90 + loseAngle / 2f, (360 - loseAngle) * (progress1 / 100f), false, progressPaint);
    }

    private void initProgress2(Canvas canvas){
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f, spanWidth);//平移画布坐标原点

        int mrg = scaleSpace + scaleWidth + 80;
        rect = new RectF(-radius + mrg, -radius + mrg, radius - mrg, radius - mrg);
        progressPaint.setStrokeWidth(30);

        progressPaint.setColor(Color.parseColor("#F5F6FA"));
        canvas.drawArc(rect, 90 + loseAngle / 2f, 360 - loseAngle, false, progressPaint);

        progressPaint.setColor(gradientProgreess2[0]);
        canvas.drawArc(rect, 90 + loseAngle / 2f, (360 - loseAngle) * (progress2 / 100f), false, progressPaint);
    }

    private void initText(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f , spanWidth);

        float rIndex = radius;

        rect = new RectF( - (rIndex/ 3 ), - (rIndex / 3), rIndex / 3, rIndex / 3);

        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        //判断指数变化及颜色设定
        textPaint.setTextSize(30);
        textPaint.setColor(Color.parseColor("#131936"));

        String desc = "我的听课时长";
        float swidth = textPaint.measureText(desc);
        //计算偏移量 是的数字和百分号整体居中显示
        swidth = swidth / 2;

        canvas.translate(swidth , 0);
        canvas.drawText(desc, 0, 0, textPaint);
        canvas.restore();
        canvas.save();

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(100);
        textPaint.setColor(Color.parseColor("#4379ff"));
        canvas.translate(canvas.getWidth()/2f  , spanWidth + radius / 4);
        canvas.drawText(String.valueOf(progress1) , 0, 0, textPaint);

    }


    private void initScale(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f, spanWidth);

        scaleTextPaint.setColor(Color.parseColor("#999999")); //小刻度画笔对象
        scaleTextPaint.setStrokeWidth(1);
        scaleTextPaint.setTextSize(35);
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);

        canvas.rotate(-(180 - loseAngle / 2f),0f,0f);

        float  y = radius;
        y = - y;

        float tempRou = (360 - loseAngle) / (float)scaleCount;

        scalePaint.setColor(Color.parseColor("#666666"));
        scalePaint.setStrokeWidth(5);

        //绘制刻度和百分比
        for (int i = 0 ; i <= scaleCount ; i++){

            canvas.drawText(String.valueOf((i) * 10), 0, y - 20f, scaleTextPaint);

            canvas.drawLine(0, y + scaleSpace , 0, y + scaleSpace + scaleWidth, scalePaint);

            canvas.rotate(tempRou,0f,0f);
        }

    }


    private void initRing(Canvas canvas) {
        scaleTextPaint.setAntiAlias(true);
        scaleTextPaint.setStrokeWidth(2);
        canvas.save();
        canvas.translate(canvas.getWidth()/2f, spanWidth);//平移画布坐标原点

        //圆环
        scaleTextPaint.setStyle(Paint.Style.FILL);
        scaleTextPaint.setColor(Color.parseColor("#f5f6fa"));
        rect = new RectF( -radius, -radius, radius, radius);
        canvas.drawArc(rect, 90 + loseAngle / 2f, 360 - loseAngle, true, scaleTextPaint);

        int with = 5;
        //内部背景色填充
        scaleTextPaint.setColor(backGroundColor);
        scaleTextPaint.setShader(null);

        rect = new RectF(-radius + with, -radius + with, radius - with, radius - with);
        canvas.drawArc(rect, 90 + loseAngle / 2f, 360 - loseAngle, true, scaleTextPaint);
    }



//    public void cgangePer(float per ){
//        this.perOld = this.per;
//        this.per = per;
//        ValueAnimator va =  ValueAnimator.ofFloat(perOld,per);
//        va.setDuration(1000);
//        va.setInterpolator(new OvershootInterpolator());
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                invalidate();
//            }
//        });
//        va.start();
//
//    }
}
