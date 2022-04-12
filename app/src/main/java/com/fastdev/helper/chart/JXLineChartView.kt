package com.fastdev.helper.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.github.mikephil.charting.charts.LineChart

/**
 * https://blog.csdn.net/ww897532167/article/details/78520548
 *  Transformer.pointValuesToPixel
 */
class JXLineChartView(context: Context, attr: AttributeSet): LineChart(context, attr){
    private var yAxisMarkerView: YAxisMarkerView? = null
    private var xAxisMarkerView: XAxisMarkerView? = null
    fun setYAxisMarker(yAxis: YAxisMarkerView){
        yAxisMarkerView = yAxis
    }
    fun setXAxisMarker(xAxis: XAxisMarkerView?){
        xAxisMarkerView = xAxis
    }
    override fun drawMarkers(canvas: Canvas?) {
        super.drawMarkers(canvas)
        // if there is no marker view or drawing marker is disabled
        if ((yAxisMarkerView == null && xAxisMarkerView == null) || !isDrawMarkersEnabled || !valuesToHighlight()) return

        for(i in 0 until mIndicesToHighlight.size){
            val highlight = mIndicesToHighlight[i]
            val set = mData.getDataSetByIndex(highlight.dataSetIndex)

            val e = mData.getEntryForHighlight(highlight)
            val entryIndex = set.getEntryIndex(e)
            // make sure entry not null
            if (e == null || entryIndex > set.entryCount * mAnimator.phaseX)
                continue

            val pos = getMarkerPosition(highlight)
            // check bounds
            if(!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue

            xAxisMarkerView?.apply {
                refreshContent(e, highlight)
                setXValue(xAxis.valueFormatter.getFormattedValue(e.x))
                draw(canvas, pos[0] - width / 2, mViewPortHandler.contentBottom())
            }

            yAxisMarkerView?.apply {
                refreshContent(e, highlight)
                draw(canvas, mViewPortHandler.contentLeft() - width,pos[1] - height / 2)
            }
        }
    }

}