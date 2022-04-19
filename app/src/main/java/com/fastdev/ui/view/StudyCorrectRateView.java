package com.fastdev.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StudyCorrectRateView extends View {
    private float centerX, centerY;
    private float radius;
    private float scaleMargin;
    private Paint paintArc;
    private Paint paintLine;
    private Paint paintScale;
    private Paint paintDesc;
    private Paint paintValue;
    private RectF rectX, rectx, rect2;
    private Path pathTriangle; //三角形
    private SweepGradient gradient1, gradient2, gradient3;
    private float[] scales = new float[]{0f, 0.6f, 0.8f, 1f};
    private String[] descs = new String[]{"不过", "飘过", "稳过"};
    private float value = 0.99f;
    private float valueAngle;
    private float textSizeTitle;
    private float textSizeValue;
    private float textValueHeight;
    private float sizex = 10;
    public StudyCorrectRateView(Context context) {
        super(context);
        initView(context);
    }

    public StudyCorrectRateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        paintArc = new Paint();
        paintArc.setStyle(Paint.Style.FILL);
        //设置渐变着色
        gradient1 = new SweepGradient(0, 0, new int[]{Color.parseColor("#FA4518"), Color.parseColor("#FFBDAC")}, null);
        gradient2 = new SweepGradient(0, 0, new int[]{Color.parseColor("#FFBA63"), Color.parseColor("#FFD8AA")}, null);
        gradient3 = new SweepGradient(0, 0, new int[]{Color.parseColor("#42D0B6"), Color.parseColor("#8DEEDC")}, null);

        paintLine = new Paint();
        paintLine.setStrokeWidth(SizeUtils.dp2px(context, 3));
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.FILL);

        paintScale = new Paint();
        paintScale.setColor(Color.parseColor("#A1A4B3"));
        paintScale.setTextSize(SizeUtils.dp2px(context, 8));
        scaleMargin = SizeUtils.dp2px(context, 5);

        paintDesc = new Paint();
        paintDesc.setColor(Color.WHITE);
        paintDesc.setStrokeWidth(SizeUtils.dp2px(context, 3));
        paintDesc.setTextSize(SizeUtils.dp2px(context, 12));

        paintValue = new Paint();
        paintValue.setColor(Color.parseColor("#131936"));
        paintValue.setStyle(Paint.Style.FILL);
        paintValue.setTextAlign(Paint.Align.CENTER);
        textSizeTitle = SizeUtils.dp2px(context, 12);
        textSizeValue = SizeUtils.dp2px(context, 24);
        Rect bounds = new Rect();
        paintValue.setTextSize(textSizeValue);
        paintValue.getTextBounds("0", 0, 1, bounds);
        textValueHeight = Math.abs(bounds.bottom - bounds.top);

        pathTriangle = new Path();

        if(scales != null && scales.length == 4){
            if(value <= scales[1]){
                valueAngle = 30;
            }else if(value <= scales[2]){
                valueAngle = 90;
            }else{
                valueAngle = 150;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h;
        radius = centerX * 4 / 5f;

        rectX = new RectF(-(radius + sizex), -(radius + sizex), (radius + sizex), (radius + sizex));
        rectx = new RectF(-radius, -radius, radius, radius);
        rect2 = new RectF(-radius / 2f, -radius / 2f, radius / 2f, radius / 2f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);//平移画布坐标原点

        //外面的刻度值
        float scaleRadius = radius + scaleMargin;

        if(scales != null && scales.length == 4){
            for (int i = 0; i < scales.length; i++){
                if(i == 0){
                    paintScale.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText((int)(scales[i] * 100) + "%", - scaleRadius, 0, paintScale);
                }else if(i == 1){
                    paintScale.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText((int)(scales[i] * 100) + "%", - scaleRadius * (float) Math.cos(Math.PI / 3f), - scaleRadius * (float) Math.sin(Math.PI / 3f), paintScale);
                }else if(i == 2){
                    paintScale.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText((int)(scales[i] * 100) + "%", scaleRadius * (float) Math.cos(Math.PI / 3f), - scaleRadius * (float) Math.sin(Math.PI / 3f), paintScale);
                }else if(i == 3){
                    paintScale.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText((int)(scales[i] * 100) + "%", scaleRadius, 0, paintScale);
                }
            }
        }

        //圆环
        paintArc.setShader(gradient1);
        canvas.drawArc(valueAngle == 30 ? rectX : rectx, 180, 60, true, paintArc);
        paintArc.setShader(gradient2);
        canvas.drawArc(valueAngle == 90 ? rectX : rectx, 240, 60, true, paintArc);
        paintArc.setShader(gradient3);
        canvas.drawArc(valueAngle == 150 ? rectX : rectx, 300, 60, true, paintArc);

        canvas.drawLine(0, 0, - (radius + sizex) * (float) Math.cos(Math.PI / 3f), - (radius + sizex) * (float) Math.sin(Math.PI / 3f), paintLine);
        canvas.drawLine(0, 0, (radius + sizex) * (float) Math.cos(Math.PI / 3f), - (radius + sizex) * (float) Math.sin(Math.PI / 3f), paintLine);

        paintArc.setShader(null);
        paintArc.setColor(Color.WHITE);
        canvas.drawArc(rect2, 180, 180, true, paintArc);

        //内容描述
        float descRadius = radius * 3 / 4 - 10;
        for (int i = 0; i < descs.length; i++) {
            paintDesc.setTextAlign(Paint.Align.CENTER);
            if(i == 0){
                canvas.drawText(descs[i], - descRadius * (float) Math.sin(Math.PI / 3), - descRadius * (float) Math.cos(Math.PI / 3), paintDesc);
            }else if(i == 1){
                canvas.drawText(descs[i], 0, - descRadius, paintDesc);
            }else if(i == 2){
                canvas.drawText(descs[i], descRadius * (float) Math.sin(Math.PI / 3), - descRadius * (float) Math.cos(Math.PI / 3), paintDesc);
            }
        }

        //绘制文字
        paintValue.setTextSize(textSizeTitle);
        canvas.drawText("总正确率", 0, -(textValueHeight + 20 + 12), paintValue);
        paintValue.setTextSize(textSizeValue);
        paintValue.setStrokeWidth(4);
        canvas.drawText((int)(value * 100) + "%", 0, -20, paintValue);

        canvas.rotate(valueAngle, 0 , 0);
        //根据参数得到旋转角度
        float pathRadius = radius / 2f;
        pathTriangle.reset();
        pathTriangle.moveTo(- pathRadius - 44 , 0);
        pathTriangle.lineTo(- pathRadius + 5 , 22);
        pathTriangle.lineTo(- pathRadius + 5 ,-22);
        pathTriangle.lineTo(- pathRadius - 44 , 0);
        pathTriangle.close();

        paintLine.setColor(Color.WHITE);
        canvas.drawPath(pathTriangle, paintLine);
        canvas.save();

    }
}
