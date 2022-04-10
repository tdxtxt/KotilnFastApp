package com.fastdev.ui.activity.main.child

import android.graphics.Color
import com.baselib.helper.StatusBarHelper
import com.baselib.helper.ToastHelper
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.fragment.BaseMvpFragment
import com.fastdev.helper.chart.ILineChartData
import com.fastdev.helper.chart.LineChartHelper
import com.fastdev.mvp.HomePresenter
import com.fastdev.ui.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
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
        val chartlineOne: List<ILineChartData> = listOf(Point("1", 2f), Point("2", 90f), Point("3", 5f), Point("4", 104f))
        val chartlineTwo: List<ILineChartData> = listOf(Point("1", 4f), Point("2", 60f), Point("3", 9f), Point("4", 6f))
        LineChartHelper(lineChart).showMultipleLineChart(mutableListOf(chartlineOne, chartlineTwo), mutableListOf("1", "2", "3", "4"), mutableListOf(Color.BLUE, Color.RED), 10)
    }

    override fun showXX() {
        ToastHelper.showToast("xxxxxx")
    }

}

class Point constructor(val x: String, val y: Float) : ILineChartData{
    override fun getLabelName(): String {
        return "$x 月"
    }

    override fun getValue(): Float {
        return y
    }

}