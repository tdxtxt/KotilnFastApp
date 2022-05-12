package com.fastdev.helper.chart;

import android.content.Context;
import android.widget.TextView;

import com.fastdev.ui.R;
import com.github.mikephil.charting.components.MarkerView;

public class XAxisMarkerView extends MarkerView {
    TextView text;
    public XAxisMarkerView(Context context) {
        super(context, R.layout.view_main_chart_market);
        text = findViewById(R.id.tv_marker);
    }

    public void showValue(int postion, String desc){
        if(text != null) text.setText(desc);
//        Log.i("markerview", "textview is empty " + (text == null)+ "; desc" + desc);
    }
}
