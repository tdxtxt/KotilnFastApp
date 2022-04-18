package com.fastdev.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StudyCorrectRateView extends View {
    private float centerX, centerY;
    private float radius;
    private Paint paintArc;
    private Paint paintLine;
    private RectF rect;
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
        paintArc.setColor(Color.RED);

        paintLine = new Paint();
        paintLine.setStrokeWidth(20);
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.FILL);
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
        centerX = w / 2;
        centerY = h;
        radius = centerX * 3 / 4f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);//平移画布坐标原点

        paintArc.setColor(Color.RED);
        rect = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rect, 180, 60, true, paintArc);

        canvas.drawArc(rect, 240, 60, true, paintArc);

        canvas.drawArc(rect, 300, 60, true, paintArc);

        canvas.drawLine(0, 0, - radius * (float) Math.cos(Math.PI / 3f), - radius * (float) Math.sin(Math.PI / 3f), paintLine);

        canvas.drawLine(0, 0, radius * (float) Math.cos(Math.PI / 3f), - radius * (float) Math.sin(Math.PI / 3f), paintLine);


        rect = new RectF(-radius / 2f, -radius / 2f, radius / 2, radius / 2);
        paintArc.setColor(Color.WHITE);
        canvas.drawArc(rect, 180, 180, false, paintArc);

    }
}
