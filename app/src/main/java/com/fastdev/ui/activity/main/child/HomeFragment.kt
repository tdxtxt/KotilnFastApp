package com.fastdev.ui.activity.main.child

import android.graphics.Color
import com.baselib.helper.StatusBarHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.fastdev.helper.chart.ILineChartData
import com.fastdev.helper.chart.LineChartHelper
import com.fastdev.helper.chart.XAxisMarkerView
import com.fastdev.helper.chart.YAxisMarkerView
import com.fastdev.mvp.HomePresenter
import com.fastdev.ui.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.android.synthetic.main.include_top_search.*
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/10
 */
@AndroidEntryPoint
class HomeFragment : BaseMvpFragment(), HomePresenter.IViewHome {
    @Inject
    lateinit var presenter: HomePresenter

    override fun createPresenter(): BaseMvpPresenter<*> {
        return presenter
    }

    override fun createMvpView(): BaseMvpView? {
        return this
    }

    override fun getLayoutId() = R.layout.fragment_main_home

    override fun initUi() {
        StatusBarHelper.setStatusBarHeight(activity, mRootView)
        lineChart.setNoDataText("你还没有记录数据");
//        lineChart.xAxis.apply {
//            setDrawAxisLine(true) //是否显示x轴线
//            axisLineColor = Color.BLUE //x轴线颜色
//            setDrawGridLines(false) //是否绘制x方向网格线
//            position = XAxis.XAxisPosition.BOTTOM
//            textSize = 12f
//            textColor = Color.BLACK
//            xOffset = 0f //偏移量
//            valueFormatter = object : ValueFormatter(){
//                val xLabels = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
//                override fun getFormattedValue(value: Float): String {
//                    if(value < 0 || value.toInt() >= xLabels.size) return ""
//                    return "${xLabels[value.toInt()]}月"
//                }
//            }
//            granularity = 1f
//            spaceMax = 1f
//        }
//        lineChart.axisLeft.apply {
//            setDrawAxisLine(true)//是否显示Y轴线
//            setDrawGridLines(false)  //是否绘制y方向网格线
//            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
//            setDrawZeroLine(true) //是否显示y轴坐标线
//            textSize = 12f
//            textColor = Color.BLACK
//            xOffset = 0f //偏移量
//            granularity = 1f
//
//            axisMaximum = 100f
//            axisMinimum = 10f
//            setLabelCount(5, false)
//        }
//
//        val lineDataSet = LineDataSet(mutableListOf(Entry(1f, 50f), Entry(2f, 60f), Entry(3f, 80f)), "")
//        lineDataSet.color = Color.BLUE // 设置曲线的颜色
//        lineDataSet.valueTextColor = Color.GREEN  //数值文字颜色
////        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER //模式为贝塞尔曲线
//        lineDataSet.setDrawCircles(true) //是否绘制圆点
//        lineDataSet.setDrawCircleHole(true)
//        lineDataSet.setCircleColor(Color.RED) //圆点颜色
//        lineDataSet.circleRadius = 3.5f //圆点半径
//        lineDataSet.isHighlightEnabled = true // 隐藏点击时候的高亮线
//        lineDataSet.highLightColor = Color.TRANSPARENT //设置高亮线为透明色
//        //填充曲线到x轴之间的区域
//        lineDataSet.setDrawFilled(true)
//        lineDataSet.fillColor = Color.YELLOW
//        lineDataSet.setLineWidth(1f) //曲线宽度
//
//        lineChart.setData(LineData(lineDataSet))
        //https://juejin.cn/post/6844904160710803464
        val chartlineOne: List<ILineChartData> = listOf(
                Point("3-31", 50f), Point("4-01", 56f),
                Point("4-02", 36f), Point("4-03", 90f),
                Point("4-04", 44f), Point("4-05", 4f),
                Point("4-06", 65f),  Point("4-07", 99f),
                Point("4-08", 55f), Point("4-09", 84f),
                Point("4-10", 35f), Point("4-11", 35f))

        val chartlineTwo: List<ILineChartData> = listOf(
                Point("3-31", 58f), Point("4-01",60f),
                Point("4-02", 38f), Point("4-03", 88f),
                Point("4-04", 42f), Point("4-05", 6f),
                Point("4-06", 60f),  Point("4-07", 100f),
                Point("4-08", 60f), Point("4-09", 85f),
                Point("4-10", 31f), Point("4-11", 34f))

        val chartlineThree: List<ILineChartData> = listOf(
                Point("4-02", 60f), Point("4-03", 60f),
                Point("4-04", 49f), Point("4-05", 16f),
                Point("4-06", 25f),  Point("4-07", 90f),
                Point("4-08", 15f), Point("4-09", 14f),
                Point("4-10", 15f), Point("4-11", 51f))


        LineChartHelper(lineChart).showMultipleLineChart(mutableListOf(chartlineOne, chartlineTwo, chartlineThree), mutableListOf(Color.argb(255, 255, 158, 44), Color.argb(255, 66, 208, 182), Color.argb(255, 67, 121, 255)), 5)
//        lineChart.marker = object : MarkerView(context, R.layout.view_chart_market) {
//            override fun refreshContent(e: Entry?, highlight: Highlight?) {
//                ToastHelper.showToast(e.toString())
//            }
//        }
        context?.also { lineChart.setYAxisMarker(YAxisMarkerView(it)); lineChart.setXAxisMarker(XAxisMarkerView(it)) }
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                ToastHelper.showToast(e.toString())
            }
            override fun onNothingSelected() {
            }
        })
    }

    override fun showXX() {
        ToastHelper.showToast("xxxxxx")
    }

}

class Point constructor(val x: String, val y: Float) : ILineChartData{
    override fun getLabelName(): String {
        return x
    }

    override fun getValue(): Float {
        return y
    }

}