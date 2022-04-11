package com.fastdev.helper.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class YAxisRendererFix extends YAxisRenderer{
    private Paint mTextPaint;
    private Rect mTextRect;
    public YAxisRendererFix(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        mTextPaint = new Paint();
        mTextRect = new Rect();
        mTextPaint.getTextBounds("A", 0, 1, mTextRect);
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        // draw
        for (int i = 0; i < mYAxis.mEntryCount; i++) {

            String text = mYAxis.getFormattedLabel(i);

            if (i == mYAxis.mEntryCount - 1) {
                //为了让最上面的Y轴lable value显示到图表里面
                offset = mTextRect.height() + Math.abs(offset) * 3;
            }
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
    }
}
