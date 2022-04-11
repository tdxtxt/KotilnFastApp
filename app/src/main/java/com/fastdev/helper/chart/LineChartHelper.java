package com.fastdev.helper.chart;
import android.graphics.Color;
import android.graphics.Matrix;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
/**
 * @author :ChenYangYi
 * @date :2018/08/03/14:19
 * @description :折线图的封装
 * @github :https://github.com/chenyy0708
 */
public class LineChartHelper {

    private LineChart lineChart;
    private YAxis yAxis;
    private XAxis xAxis;

    public  LineChartHelper(LineChart lineChart) {
        this.lineChart = lineChart;
        yAxis = lineChart.getAxisLeft();
        xAxis = lineChart.getXAxis();
        //背景颜色
        lineChart.setBackgroundColor(Color.WHITE);
        // 隐藏右边Y轴
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setDoubleTapToZoomEnabled(false);
        //设置描述
        lineChart.getDescription().setEnabled(false);
        //设置按比例放缩柱状图
        lineChart.setPinchZoom(true);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
    }


    /**
     * 初始化LineChart
     */
    private void initLineChart() {
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);//不显示图例
//        legend.setForm(Legend.LegendForm.LINE);
//        legend.setTextSize(11f);
        //显示位置
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);
        //x坐标轴设置
        XAxis xAxis = lineChart.getXAxis();
        // x轴对齐位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 隐藏网格线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
//        lineChart.setRendererLeftYAxis(new YAxisRendererFix(lineChart.getViewPortHandler(), yAxis, lineChart.getTransformer(yAxis.getAxisDependency())));
        //y轴设置
        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setLabelCount(10, false);
        leftAxis.setAxisMaximum(100);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceMin(10f);
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);
        lineChart.getXAxis().setAxisMinimum(0);
    }

    /**
     * 展示折线图(多条)
     *
     * @param lineSetData    多个折线图数据
     * @param barColors      颜色
     * @param singleBarChart 一页显示个数
     */
    public void showMultipleLineChart(List<List<ILineChartData>> lineSetData, List<Integer> barColors, int singleBarChart) {
        // 初始化图表X、Y轴属性
        initLineChart();
        // X轴真实显示lable
        List<String> xValue = new ArrayList<>();
        // 多折线图数据集
        LineData lineData = new LineData();
        // 多折线图循环，得到每一种折线图集合数据
        float yMinValue = lineSetData.get(0).get(0).getValue();
        for (int i = 0; i < lineSetData.size(); i++) {
            List<ILineChartData> barSetDatum = lineSetData.get(i);
            // 单种柱状图数据集
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0; j < barSetDatum.size(); j++) {
                if (!xValue.contains(barSetDatum.get(j).getLabelName())) {
                    xValue.add(barSetDatum.get(j).getLabelName());
                }
                yMinValue = Math.min(yMinValue, barSetDatum.get(i).getValue());
                entries.add(new Entry(j, barSetDatum.get(j).getValue()));
            }
            // 数据集合标签名
            LineDataSet barDataSet = new LineDataSet(entries, "第" + i + "条线"/*(labels == null) ? "" : labels.get(i)*/);
            barDataSet.setColor(barColors.get(i));
            barDataSet.setValueTextColor(barColors.get(i));
            barDataSet.setValueTextSize(10f);
            //设置直线图填充
            barDataSet.setDrawFilled(true);
            //设置填充颜色
            barDataSet.setFillColor(Color.parseColor("#FFA2A2"));
            //显示圆点
            barDataSet.setDrawCircles(true);
            //设置圆点颜色(外圈)
            barDataSet.setCircleColor(barColors.get(i));
            //设置圆点填充颜色
            barDataSet.setCircleHoleColor(Color.parseColor("#FFFFFF"));
            //设置线条为平滑曲线
            barDataSet.setMode(LineDataSet.Mode.LINEAR);
            //不显示曲线点的具体数值
            barDataSet.setDrawValues(false);
            //选中十字架样式
            barDataSet.setHighLightColor(barColors.get(i));
            barDataSet.setHighlightEnabled(true);
            barDataSet.setHighlightLineWidth(0.5f);
            barDataSet.enableDashedHighlightLine(3, 5, 0);

            lineData.addDataSet(barDataSet);
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));
        lineChart.setData(lineData);
        //设置动画效果
        Matrix m = new Matrix();
        //两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的2倍
        if (xValue.size() > singleBarChart) {
//            m.postScale(xValue.size() / singleBarChart, 1f);
        }
        //将图表动画显示之前进行缩放
        lineChart.getViewPortHandler().refresh(m, lineChart, false);
        lineChart.animateY(1500, Easing.Linear);
        lineChart.animateX(1000, Easing.Linear);
    }


    /**
     * 展示折线图(一条)
     *
     * @param barChartData 单个柱状图数据
     * @param color        柱状图颜色
     * @param displayCount 一页显示柱状图数
     */
    public void showSingleLineChart(List<ILineChartData> barChartData, int color, int displayCount) {
        initLineChart();
        // X轴真实显示lable
        List<String> xValue = new ArrayList<>();
        // Y轴图标数据
        // 单种柱状图数据集
        ArrayList<Entry> entries = new ArrayList<>();
        for (int j = 0; j < barChartData.size(); j++) {
            if (!xValue.contains(barChartData.get(j).getLabelName())) {
                xValue.add(barChartData.get(j).getLabelName());
            }
            entries.add(new Entry(j, barChartData.get(j).getValue()));
        }
        // 多折线图数据集
        LineData lineData = new LineData();
        //折线图例 标签 设置
        lineChart.getLegend().setEnabled(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));
        // 数据集合标签名
        LineDataSet barDataSet = new LineDataSet(entries, "");
        barDataSet.setColor(color);
        barDataSet.setValueTextColor(color);
        barDataSet.setValueTextSize(10f);
        barDataSet.setDrawCircles(false);
        lineData.addDataSet(barDataSet);
        lineChart.setData(lineData);
        //设置动画效果
        Matrix m = new Matrix();
        //两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的2倍
        if (xValue.size() > displayCount) {
            m.postScale(xValue.size() / displayCount, 1f);
        }
        //将图表动画显示之前进行缩放
        lineChart.getViewPortHandler().refresh(m, lineChart, false);
        lineChart.animateY(1500, Easing.Linear);
        lineChart.animateX(1000, Easing.Linear);
    }
}