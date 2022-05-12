package com.fastdev.helper.chart;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fastdev.ui.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.utils.MPPointF;

public class YAxisMarkerView extends MarkerView {
    TextView text;
    public YAxisMarkerView(Context context) {
        super(context, R.layout.view_main_chart_market);
        text = findViewById(R.id.tv_marker);
    }

    public void showValue(int postion, String desc){
        if(View.VISIBLE != text.getVisibility()) text.setVisibility(View.VISIBLE);
       if(text != null) text.setText(desc);
    }

    public void hide(){
        text.setVisibility(View.GONE);
    }

    @Override
    public MPPointF getOffset() {
//        return new MPPointF(-(getWidth() / 2f), - getHeight());
        return super.getOffset();
    }
}
