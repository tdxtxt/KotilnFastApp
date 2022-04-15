package com.fastdev.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

//半圆环 https://github.com/kong-jing/CircleProgressView
//齿轮线 https://juejin.cn/post/7043705977538871332
//齿轮半圆环 https://juejin.cn/post/6844903777548386318
// https://github.com/HotBitmapGG/CreditSesameRingView
// https://github.com/clwater/AndroidDashBoard/blob/master/app/src/main/java/clwater/androiddashboard/DashBoard.java
public class StudyTimeChartView extends View {
    private Paint paint , tmpPaint , textPaint ,  strokePain;
    private RectF rect;
    private int backGroundColor;    //背景色
    private float per ;             //指数百分比
    private float perOld ;          //变化前指针百分比
    private float length ;          //仪表盘半径
    private float r ;
    private int loseAngle = 60;          //失去的角度
    private int scaleCount = 10;


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

        int heitht = (int) (width / 2 * (1 + Math.cos(Math.toRadians(loseAngle / 2))));
        initIndex(width / 2);
        //优化组件高度
        setMeasuredDimension(width, heitht);
    }

    private void initIndex(int specSize) {
        backGroundColor = Color.WHITE;
        r = specSize;
        length = r  / 4 * 3;
        per = 0;
        perOld = 0;
    }

    public void setR(float r) {
        this.r = r;
        this.length = r  / 4 * 3;
    }

    private void init() {
        paint = new Paint();
        rect = new RectF();
        textPaint = new Paint();
        tmpPaint = new Paint();
        strokePain = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //颜色指示的环
        initRing(canvas);
        //刻度文字
        initScale(canvas);
        //提示内容
        initText(canvas);
    }

    private void initText(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);

        float rIndex = length ;

        //设置文字展示的圆环
        paint.setColor(Color.parseColor("#eeeeee"));
        paint.setShader(null);
        paint.setShadowLayer(5, 0, 0, 0x54000000);
        rect = new RectF( - (rIndex/ 3 ), - (rIndex / 3), rIndex / 3, rIndex / 3);
        canvas.drawArc(rect, 0, 360, true, paint);

        paint.clearShadowLayer();

        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2f , r);


        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);

        textPaint.setTextSize(60);
        textPaint.setColor(Color.parseColor("#fc6555"));
        textPaint.setTextAlign(Paint.Align.RIGHT);


        //判断指数变化及颜色设定

        int _per = (int) (per * 120);

        if (_per < 60){
            textPaint.setColor(Color.parseColor("#ff6450"));
        }else if (_per < 100) {
            textPaint.setColor(Color.parseColor("#f5a623"));
        }else {
            textPaint.setColor(Color.parseColor("#79d062"));
        }

        float swidth = textPaint.measureText(String.valueOf(_per));
        //计算偏移量 是的数字和百分号整体居中显示
        swidth =   (swidth - (swidth + 22) / 2);


        canvas.translate( swidth , 0);
        canvas.drawText("" + _per, 0, 0, textPaint);

        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("%" , 0, 0, textPaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#999999"));


        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2  , r + length / 3 /2 );
        canvas.drawText("完成率" , 0, 0, textPaint);



    }


    public void setBackGroundColor(int color){
        this.backGroundColor = color;
    }

    private void initScale(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);
        paint.setColor(Color.parseColor("#999999"));

        tmpPaint = new Paint(paint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(1);
        tmpPaint.setTextSize(35);
        tmpPaint.setTextAlign(Paint.Align.CENTER);



        canvas.rotate(-(180 - loseAngle / 2f),0f,0f);

        float  y = length;
        y = - y;
        paint.setColor(backGroundColor);

        float tempRou = (360 - loseAngle) / (float)scaleCount;

        paint.setColor(Color.parseColor("#666666"));
        paint.setStrokeWidth(5);

        int scaleWidth = 20; //刻度线宽度
        int scaleSpace = 30; //刻度线距离外圆间距
        //绘制刻度和百分比
        for (int i = 0 ; i <= scaleCount ; i++){

            if (i % 2 == 0 ) {
                canvas.drawText(String.valueOf((i) * 10), 0, y - 20f, tmpPaint);
            }

            canvas.drawLine(0, y + scaleSpace , 0, y + scaleSpace + scaleWidth, paint);

            canvas.rotate(tempRou,0f,0f);
        }

    }


    private void initRing(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        canvas.restore();
        canvas.save();
        canvas.translate(canvas.getWidth()/2, r);//平移画布坐标原点

        //圆环
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#F95A37"));
        rect = new RectF( -length, -length, length, length);
        canvas.drawArc(rect, 90 + loseAngle / 2, 360 - loseAngle, true, paint);

        int with = 5;
        //内部背景色填充
        paint.setColor(backGroundColor);
        paint.setShader(null);

        rect = new RectF(-length + with, -length + with, length - with, length - with);
        canvas.drawArc(rect, 90 + loseAngle / 2, 360 - loseAngle, true, paint);
    }



    public void cgangePer(float per ){
        this.perOld = this.per;
        this.per = per;
        ValueAnimator va =  ValueAnimator.ofFloat(perOld,per);
        va.setDuration(1000);
        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        va.start();

    }
}
