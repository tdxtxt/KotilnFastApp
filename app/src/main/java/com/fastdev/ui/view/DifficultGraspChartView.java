package com.fastdev.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.baselib.ui.view.container.round.DensityUtil;
import com.fastdev.ui.R;

public class DifficultGraspChartView extends View {
    private int count = 5; //几边形
    private int layerCount = 4; //层数
    private float angle; //每条边对应的圆心角
    private int centerX; //圆心x
    private int centerY; //圆心y
    private float radius; //半径
    private Paint linePaint; //连线paint
    private Paint txtPaint; //文字paint
    private Paint circlePaint; //圆点paint
    private Paint regionColorPaint; //覆盖区域paint
    private Double[] percents1 = {0.91, 0.35, 0.72, 0.8, 0.5}; //覆盖区域百分比1
    private Double[] percents2 = {0.63, 0.20, 0.55, 0.9, 0.8}; //覆盖区域百分比2
    private String[] titles = {"dota","斗地主","大吉大利，晚上吃鸡","炉石传说","跳一跳"};//文字

    public DifficultGraspChartView(Context context) {
        super(context);
        initView(context);
    }

    public DifficultGraspChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        //计算圆心角
        angle = 360f / count;

        linePaint = new Paint();
        linePaint.setColor(ContextCompat.getColor(context, R.color.bule_4379ff));
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);

        txtPaint = new Paint();
        txtPaint.setColor(ContextCompat.getColor(context, R.color.black_000000));
        txtPaint.setAntiAlias(true);
        txtPaint.setStyle(Paint.Style.STROKE);
        txtPaint.setTextSize(DensityUtil.dip2px(context, 12));

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);

        regionColorPaint = new Paint();
//        regionColorPaint.setColor(ContextCompat.getColor(context, R.color.red_ff852b));
        regionColorPaint.setAlpha(200);
        regionColorPaint.setStyle(Paint.Style.FILL);
        regionColorPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);//画边
        drawLines(canvas);//画线
        drawText(canvas);//描绘文字
        drawRegion(canvas);//覆盖区域
    }

    /**
     * 需要正五边形得有一个圆，圆内接正五边形，在onSizeChanged方法里获取圆心，确定半径
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(h, w) / 2f * 0.7f;
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //优化组件高度
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    /**
     * 绘制多边形
     */
    private void drawPolygon(Canvas canvas) {
        canvas.save();
        canvas.translate(centerX, centerY);//平移画布坐标原点
        int width = 4;
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(width);
        for (int i = 1; i <= layerCount; i++) {
            if(i == layerCount){
                circlePaint.setColor(Color.parseColor("#7ba1ff"));
                circlePaint.setPathEffect(null);
            }else{
                circlePaint.setColor(Color.parseColor("#c4d0ff"));
                circlePaint.setPathEffect(new DashPathEffect(new float[] { 10, 10 }, 0));
            }
            canvas.drawCircle(0, 0, i * (radius / (float)layerCount), circlePaint);
        }
    }

    /**
     * 绘制连线
     */
    private void drawLines(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(centerX, centerY);

        for (int i = 0; i < count; i++) {
            double degrees;
            if(count == 5){
                degrees = (angle - (90 - angle)) + angle * i;
            }else{
                degrees = angle * i;
            }
            float endX = (float) (Math.cos(Math.toRadians(degrees)) * radius);
            float endY = (float) (Math.sin(Math.toRadians(degrees)) * radius);
            canvas.drawLine(0, 0, endX, endY, linePaint);
        }
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(centerX, centerY);
        int marginText = 20;
        for (int i = 0; i < count; i++) {
            float degrees;
            if(count == 5){
                degrees = (angle - (90 - angle)) + angle * i;
            }else{
                degrees = angle * i;
            }

            degrees = degrees % 360f;

            //获取到雷达图最外边的坐标
            float x = (float) (Math.cos(Math.toRadians(degrees)) * (radius + marginText));
            float y = (float) (Math.sin(Math.toRadians(degrees)) * (radius + marginText));
            String txt = titles[i];
            if(0 <= degrees && degrees < 90){//0-90度,右下方
                txtPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(txt, x, y, txtPaint);
            }else if(degrees == 90){ //90度，正下方
                txtPaint.setTextAlign(Paint.Align.CENTER);
                Rect bounds = new Rect();
                txtPaint.getTextBounds(txt, 0, txt.length(), bounds);
//                float txtWidth = txtPaint.measureText(txt);
                float txtHeight = bounds.bottom - bounds.top;
                canvas.drawText(txt, x, y + txtHeight, txtPaint);
            }else if(90 < degrees && degrees <= 180){//90-180度,左下方
                txtPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(txt, x, y, txtPaint);
            }else if(180 < degrees && degrees < 270) {//180-270度,左上方
                txtPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(txt, x, y, txtPaint);
            }else if(degrees == 270){//正上方
                txtPaint.setTextAlign(Paint.Align.CENTER);
                Rect bounds = new Rect();
                txtPaint.getTextBounds(txt, 0, txt.length(), bounds);
                float txtHeight = bounds.bottom - bounds.top;
                canvas.drawText(txt, x, y - txtHeight / 2f, txtPaint);
            }else if(270 < degrees && degrees < 360){//右上方
                txtPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(txt, x, y, txtPaint);
            }
        }
    }

    /**
     * 绘制区域
     */
    private void drawRegion(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.translate(centerX, centerY);

        Path path1 = new Path();
        Path path2 = new Path();
        for (int i = 0; i < count; i++) {
            float degrees;
            if (count == 5) {
                degrees = (angle - (90 - angle)) + angle * i;
            } else {
                degrees = angle * i;
            }

            //获取到雷达图最外边的坐标
            float x1 = (float) (Math.cos(Math.toRadians(degrees)) * (radius * percents1[i]));
            float y1 = (float) (Math.sin(Math.toRadians(degrees)) * (radius * percents1[i]));

            float x2 = (float) (Math.cos(Math.toRadians(degrees)) * (radius * percents2[i]));
            float y2 = (float) (Math.sin(Math.toRadians(degrees)) * (radius * percents2[i]));

            if(i == 0){
                path1.moveTo(x1, y1);
                path2.moveTo(x2, y2);
            }else{
                path1.lineTo(x1, y1);
                path2.lineTo(x2, y2);
            }
        }
        path1.close();
        RadialGradient gradient = new RadialGradient(0f, 0f, radius, Color.parseColor("#95ACFF"), Color.parseColor("#4379FF"), Shader.TileMode.MIRROR);
        regionColorPaint.setShader(gradient);
        canvas.drawPath(path1, regionColorPaint);

        RadialGradient gradient2 = new RadialGradient(0f, 0f, radius, Color.parseColor("#A2F3DB"), Color.parseColor("#42D0B6"), Shader.TileMode.MIRROR);
        regionColorPaint.setShader(gradient2);
        canvas.drawPath(path2, regionColorPaint);
    }


//    //设置几边形，**注意：设置几边形需要重新计算圆心角**
//    public void setCount(int count){
//        this.count = count;
//        angle = (float) (Math.PI * 2 / count);
//        invalidate();
//    }
//
//    //设置层数
//    public void setLayerCount(int layerCount){
//        this.layerCount = layerCount;
//        invalidate();
//    }
}
