package com.fastdev.helper.chart

import android.content.Context
import android.widget.TextView
import com.fastdev.ui.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class XAxisMarkerView(context: Context) : MarkerView(context, R.layout.view_chart_market) {
    private var tvMarker: TextView? = null
    init{
        tvMarker = findViewById(R.id.tv_marker)
    }

    fun setXValue(data: String){
        tvMarker?.text = data
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
    }
}